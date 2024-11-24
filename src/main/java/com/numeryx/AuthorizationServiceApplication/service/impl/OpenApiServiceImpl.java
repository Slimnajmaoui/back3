package com.numeryx.AuthorizationServiceApplication.service.impl;

import com.numeryx.AuthorizationServiceApplication.client.NotificationFeignClient;
import com.numeryx.AuthorizationServiceApplication.dto.UserDto;
import com.numeryx.AuthorizationServiceApplication.dto.request.ChangeUserConfidentiality;
import com.numeryx.AuthorizationServiceApplication.dto.request.ChangeUserCredentials;
import com.numeryx.AuthorizationServiceApplication.dto.request.ResetTokenRequest;
import com.numeryx.AuthorizationServiceApplication.enumeration.RoleEnum;
import com.numeryx.AuthorizationServiceApplication.exception.*;
import com.numeryx.AuthorizationServiceApplication.mapper.UserResponseMapper;
import com.numeryx.AuthorizationServiceApplication.model.Subscriber;
import com.numeryx.AuthorizationServiceApplication.model.SubscriberAccount;
import com.numeryx.AuthorizationServiceApplication.model.Token;
import com.numeryx.AuthorizationServiceApplication.model.User;
import com.numeryx.AuthorizationServiceApplication.repository.ISubscriberAccountRepository;
import com.numeryx.AuthorizationServiceApplication.repository.TokenRepository;
import com.numeryx.AuthorizationServiceApplication.repository.UserRepository;
import com.numeryx.AuthorizationServiceApplication.security.EntryPointSecurityHandler;
import com.numeryx.AuthorizationServiceApplication.service.IOpenApiService;
import com.numeryx.AuthorizationServiceApplication.utilities.Constant;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import static com.numeryx.AuthorizationServiceApplication.utilities.Constant.*;

@Service
public class OpenApiServiceImpl implements IOpenApiService {

    private final Logger logger = LoggerFactory.getLogger(OpenApiServiceImpl.class);


    @Value("${security.jwt.token-timeout}")
    private Long tokenTimeout;


    private final NotificationFeignClient notificationFeignClient;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    private final UserResponseMapper userResponseMapper;
    private final EntryPointSecurityHandler entryPointSecurityHandler;
    @Autowired
    private ISubscriberAccountRepository subscriberAccountRepository;

    public OpenApiServiceImpl(NotificationFeignClient notificationFeignClient,
                              TokenRepository tokenRepository,
                              UserRepository userRepository,
                              UserResponseMapper userResponseMapper,
                              EntryPointSecurityHandler entryPointSecurityHandler) {
        this.notificationFeignClient = notificationFeignClient;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
        this.entryPointSecurityHandler = entryPointSecurityHandler;
    }

    @Override
    public void updateUserCredentials(ChangeUserCredentials changeUserCredentials){
        logger.debug("Request to update user credentials '{}'", changeUserCredentials);
        Token tokenDB = tokenRepository.findTokenByMailTokenAndIdUser_Id(changeUserCredentials.getToken(), changeUserCredentials.getId());
        if (tokenDB == null) {
            throw new TokenException(Collections.singletonList(new Reason(TokenException.ReasonCode.TOKEN_EXPIRED, TOKEN_EXPIRED)));
        }
        if(tokenDB.getMailToken().equals(changeUserCredentials.getToken())) {

            Optional<User> currentUser = userRepository.findById(changeUserCredentials.getId());
            currentUser.map(el ->{
                this.entryPointSecurityHandler.checkHeader(el.getRole());
                el.setEnabled(true);
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                el.setPassword(passwordEncoder.encode(changeUserCredentials.getPassword()));
                tokenDB.setEnabledToken(false);
                return  userRepository.save(el);
            }).orElseThrow(() -> new EntityNotFoundException(Collections.singletonList(new Reason(EntityNotFoundException.ReasonCode.USER_NOT_FOUND, USER_NOT_FOUND_EXCEPTION))));
        } else {
            throw new TokenException(Collections.singletonList(new Reason(TokenException.ReasonCode.VALIDATION_TOKEN_EXPIRED, VALIDATION_TOKEN_EXPIRED)));
        }
    }

    @Override
    public void resetPassword(String username) {
        logger.debug("Request to reset user Password '{}'", username);
        User entity = userRepository.findByUsernameIgnoreCase(username);
        if(entity == null) {
            throw new EntityNotFoundException(Collections.singletonList(new Reason(EntityNotFoundException.ReasonCode.INVALID_EMAIL, INVALID_EMAIL_USER_EXCEPTION)));
        }
        this.entryPointSecurityHandler.checkHeader(entity.getRole());
        String token = UUID.randomUUID().toString();
        Token tokenDB = new Token();
        tokenDB.setMailToken(token);
        tokenDB.setMailTokenCreationDate(new Date());
        tokenDB.setIdUser(entity);
        tokenDB.setEnabledToken(true);
        this.tokenRepository.save(tokenDB);
        sendResetPasswordMethod(entity, token);
    }

    @Override
    public Boolean checkActivationCode(Long id, String reqToken, String activationCode, boolean isResetRequest) {
        Token token = tokenRepository.findTokenByMailTokenAndIdUser_Id(reqToken, id);
        if (token == null) {
            throw new TokenException(Collections.singletonList(new Reason(TokenException.ReasonCode.VALIDATION_CODE_EXPIRED, VALIDATION_CODE_EXPIRED)));
        }
        if (token.getActivationCodeCreationDate() != null) {
            Date validity = new Date(token.getActivationCodeCreationDate().getTime() + tokenTimeout);
            if (new Date().after(validity)) {
                token.setEnabledCode(false);
                throw new TokenException(Collections.singletonList(new Reason(TokenException.ReasonCode.VALIDATION_CODE_EXPIRED, VALIDATION_CODE_EXPIRED)));
            }
        }
        String activationCodeChecked = token.getActivationCode();
        boolean res = activationCodeChecked != null && activationCodeChecked.equals(activationCode) && token.isEnabledCode();
        if (res) {
            // action (password request, password reset, mail reset)
            // validated after submitting the right activation code
            // => code is no longer valid, token needs to be valid for the next step: set password
            token.setEnabledCode(false);
            tokenRepository.save(token);
        } else {
            //if activation code is wrong => token and code can still be valid
            int nbFailedAttemptActivationCode = token.getNbFailedAttemptActivationCode() + 1;
            if(nbFailedAttemptActivationCode > Constant.NB_FAILED_ATTEMPT_ACTIVATION_CODE) {
                token.setEnabledToken(false);
                token.setEnabledCode(false);
                User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found", Arrays.asList((new Reason(UsernameNotFoundException.ReasonCode.USER_NOT_FOUND, USER_NOT_FOUND_EXCEPTION)))));
                tokenRepository.delete(token);
                if(!isResetRequest) {
                    userRepository.delete(user);
                }
                throw new TokenException(Collections.singletonList(new Reason(TokenException.ReasonCode.FAILED_CREATED_COMPTE_EXCEPTION, FAILED_CREATED_COMPTE_EXCEPTION)));
            } else {
                token.setNbFailedAttemptActivationCode(nbFailedAttemptActivationCode);
                tokenRepository.save(token);
                throw new TokenException(Collections.singletonList(new Reason(TokenException.ReasonCode.WRONG_VALIDATION_CODE_EXCEPTION, WRONG_VALIDATION_CODE_EXCEPTION)));
            }
        }
        return true;
    }

    @Override
    public Boolean checkToken(Long id, String token, boolean hasCode, boolean isResetRequest) {
        Token tokenDB = tokenRepository.findTokenByMailTokenAndIdUser_Id(token, id);
        if (tokenDB == null) {
            throw new TokenException(Collections.singletonList(new Reason(TokenException.ReasonCode.VALIDATION_CODE_EXPIRED, VALIDATION_CODE_EXPIRED)));
        }
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found", Arrays.asList((new Reason(UsernameNotFoundException.ReasonCode.USER_NOT_FOUND, USER_NOT_FOUND_EXCEPTION)))));
        this.entryPointSecurityHandler.checkHeader(user.getRole());
        if (tokenDB.getMailTokenCreationDate() != null) {
            Date validity = new Date(tokenDB.getMailTokenCreationDate().getTime() + tokenTimeout);

            if (new Date().after(validity)) {
                SubscriberAccount subscriberAccount = ((SubscriberAccount) user);

                if(subscriberAccount.getSubscriber() != null){
                    subscriberAccount.getSubscriber().setHasAccount(false);
                    subscriberAccountRepository.saveAndFlush(subscriberAccount);
                }
                tokenDB.setEnabledToken(false);
                tokenDB.setEnabledCode(false);
                tokenRepository.delete(tokenDB);
                if(!isResetRequest) {
                    userRepository.delete(user);
                }
                throw new TokenException(Collections.singletonList(new Reason(TokenException.ReasonCode.VALIDATION_CODE_EXPIRED, VALIDATION_CODE_EXPIRED)));
            }
        }
        String tokenChecked = tokenDB.getMailToken();
        Boolean res = tokenChecked != null && tokenChecked.equals(token) && tokenDB.isEnabledToken();
        if(hasCode) {
            // In case there is a validation code: Token can still be valid if time < token-timeout, user can reload page...
            // so we won't set it to enabled if res = true unless the activation code has been submitted
            if (!res) {
                tokenDB.setEnabledToken(false);
                tokenRepository.save(tokenDB);
                throw new TokenException(Collections.singletonList(new Reason(TokenException.ReasonCode.VALIDATION_CODE_EXPIRED, VALIDATION_CODE_EXPIRED)));
            }
        } else {
            // In case there is no validation code,
            // token page is accessed only once
            // and needs to be deactivated
            tokenDB.setEnabledToken(false);
            tokenRepository.save(tokenDB);
        }
        return res;
    }

    @Override
    public UserDto sendActivationCode(Long id, String token) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found", Collections.singletonList((new Reason(UsernameNotFoundException.ReasonCode.USER_NOT_FOUND, USER_NOT_FOUND_EXCEPTION)))));
        this.entryPointSecurityHandler.checkHeader(user.getRole());
        Token tokenDB = tokenRepository.findTokenByMailTokenAndIdUser_Id(token, id);
        if(tokenDB == null) {
            throw new TokenException(Collections.singletonList(new Reason(TokenException.ReasonCode.TOKEN_EXPIRED, TOKEN_EXPIRED)));
        }
        String activationCode = RandomStringUtils.randomNumeric(4);
        tokenDB.setActivationCode(activationCode);
        tokenDB.setActivationCodeCreationDate(new Date());
        tokenDB.setEnabledCode(true);
        tokenRepository.save(tokenDB);
        sendActivationCodeMethod(user, activationCode);
        UserDto userDto = userResponseMapper.toDto(user);
        userDto.setEnabledSendActivationCode(true);
        return userDto;
    }

    @Override
    public UserDto resendActivationCode(Long id, String token) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found", Collections.singletonList((new Reason(UsernameNotFoundException.ReasonCode.USER_NOT_FOUND, USER_NOT_FOUND_EXCEPTION)))));

        this.entryPointSecurityHandler.checkHeader(user.getRole());
        Token tokenDB = tokenRepository.findTokenByMailTokenAndIdUser_Id(token, id);
        if(tokenDB == null) {
            throw new TokenException(Collections.singletonList(new Reason(TokenException.ReasonCode.TOKEN_EXPIRED, TOKEN_EXPIRED)));
        }
        String activationCode = RandomStringUtils.randomNumeric(4);
        tokenDB.setActivationCode(activationCode);
        tokenDB.setActivationCodeCreationDate(new Date());
        tokenDB.setEnabledCode(true);
        int nbSendActivationCodeAttempt = tokenDB.getNbSendActivationCodeAttempt() + 1;
        tokenDB.setNbSendActivationCodeAttempt(nbSendActivationCodeAttempt);
        tokenRepository.save(tokenDB);
        sendActivationCodeMethod(user, activationCode);
        UserDto userDto = userResponseMapper.toDto(user);
        userDto.setEnabledSendActivationCode(true);
        if (nbSendActivationCodeAttempt > NB_RESEND_ACTIVATION_CODE_ATTEMPT) {
            userDto.setEnabledSendActivationCode(false);
        }
        return userDto;
    }

    @Override
    public RoleEnum getRoleByIdAndToken(Long id, String token) {
        logger.debug("Request to get user role by userId and token {}, {}", id, token);
        Token t = tokenRepository.findTokenByMailTokenAndIdUser_Id(token, id);
        if(t == null) {
            throw new TokenException((List<Reason>) new Reason(TokenException.ReasonCode.WRONG_TOKEN, WRONG_TOKEN_EXCEPTION));
        }
        return t.getIdUser().getRole();
    }

    @Override
    public UserDto validateUserEmail(ChangeUserConfidentiality changeUserConfidentiality) {
        logger.debug("Request to validate User Email : {}", changeUserConfidentiality);
        User userToUpdate= userRepository.findById(changeUserConfidentiality.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found", Collections.singletonList((new Reason(UsernameNotFoundException.ReasonCode.USER_NOT_FOUND, USER_NOT_FOUND_EXCEPTION)))));

        this.entryPointSecurityHandler.checkHeader(userToUpdate.getRole());
        Token tokenDB = tokenRepository.findTokenByMailTokenAndIdUser_Id(changeUserConfidentiality.getToken(), changeUserConfidentiality.getId());
        if(tokenDB == null) {
            throw new TokenException(Collections.singletonList(new Reason(TokenException.ReasonCode.TOKEN_EXPIRED, TOKEN_EXPIRED)));
        }
        if (tokenDB.getMailTokenCreationDate() != null) {
            Date validity = new Date(tokenDB.getMailTokenCreationDate().getTime() + tokenTimeout);
            if (new Date().after(validity)) {
                tokenDB.setEnabledToken(false);
                tokenRepository.save(tokenDB);
                throw new TokenException(Collections.singletonList(new Reason(TokenException.ReasonCode.VALIDATION_CODE_EXPIRED, VALIDATION_CODE_EXPIRED)));
            }
        }
        String tokenChecked = tokenDB.getMailToken();
        boolean res = tokenChecked != null && tokenChecked.equals(changeUserConfidentiality.getToken()) && tokenDB.isEnabledToken();
        tokenDB.setEnabledToken(false);
        tokenRepository.save(tokenDB);
        if (!res) {
            throw new TokenException(Collections.singletonList(new Reason(TokenException.ReasonCode.VALIDATION_CODE_EXPIRED, VALIDATION_CODE_EXPIRED)));
        }
        String oldUsername = userToUpdate.getUsername();
        userToUpdate.setUsername(userToUpdate.getUsernameChange());
        userToUpdate.setUsernameChange(oldUsername);
        sendChangeEmailMethod(userToUpdate, null);
        return userResponseMapper.toDto(userRepository.save(userToUpdate));
    }


    @Async
    public void sendResetPasswordMethod(User entity, String token){
        this.entryPointSecurityHandler.checkHeader(entity.getRole());
        ResetTokenRequest resetPasswordRequest = setMailRequest(entity, token, false);
        if(entity.getRole().equals(RoleEnum.ROLE_ADMIN_SUBSCRIBER) ||
                entity.getRole().equals(RoleEnum.ROLE_USER_SUBSCRIBER)) {
            this.notificationFeignClient.sendResetPasswordForSubscriber(resetPasswordRequest);
        }else
            this.notificationFeignClient.sendResetPassword(resetPasswordRequest);

    }

    @Async
    public void sendActivationCodeMethod(User entity, String token){
        this.entryPointSecurityHandler.checkHeader(entity.getRole());
        ResetTokenRequest resetPasswordRequest = setMailRequest(entity, token, false);
        this.notificationFeignClient.sendActivationCode(resetPasswordRequest);
    }

    @Async
    public void sendChangePasswordMethod(User entity, String token){
        this.entryPointSecurityHandler.checkHeader(entity.getRole());
        ResetTokenRequest changePasswordRequest = setMailRequest(entity, token, false);
        this.notificationFeignClient.sendChangePasswordInfoEmail(changePasswordRequest);
    }

    @Async
    public void sendChangePhoneMethod(User entity, String token){
        ResetTokenRequest changePasswordRequest = setMailRequest(entity, token, false);
        this.notificationFeignClient.sendChangePhoneInfoEmail(changePasswordRequest);
    }

    @Async
    public void sendChangeEmailMethod(User entity, String token){
        this.entryPointSecurityHandler.checkHeader(entity.getRole());
        ResetTokenRequest changeEmailRequest = setMailRequest(entity, token, false);
        this.notificationFeignClient.sendChangeEmailInfoEmail(changeEmailRequest);
    }

    private ResetTokenRequest setMailRequest(User entity, String token, Boolean changeMail) {
        this.entryPointSecurityHandler.checkHeader(entity.getRole());
        ResetTokenRequest resetPasswordRequest = new ResetTokenRequest();
        if(changeMail) {
            resetPasswordRequest.setEmail(entity.getUsernameChange());
        } else {
            resetPasswordRequest.setEmail(entity.getUsername());
        }
        resetPasswordRequest.setToken(token);
        resetPasswordRequest.setUserFirstName(entity.getFirstname() + " " + entity.getLastname());
        resetPasswordRequest.setId(entity.getId());
        return resetPasswordRequest;
    }
}
