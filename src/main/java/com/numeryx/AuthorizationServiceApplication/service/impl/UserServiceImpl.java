package com.numeryx.AuthorizationServiceApplication.service.impl;

import com.numeryx.AuthorizationServiceApplication.client.NotificationFeignClient;
import com.numeryx.AuthorizationServiceApplication.config.TokenDataWrapper;
import com.numeryx.AuthorizationServiceApplication.dto.MinifiedUserDTO;
import com.numeryx.AuthorizationServiceApplication.dto.UserDto;
import com.numeryx.AuthorizationServiceApplication.dto.request.ChangeUserConfidentiality;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateUserRequest;
import com.numeryx.AuthorizationServiceApplication.dto.request.ResetTokenRequest;
import com.numeryx.AuthorizationServiceApplication.dto.request.UpdateUserProfileRequest;
import com.numeryx.AuthorizationServiceApplication.enumeration.RoleEnum;
import com.numeryx.AuthorizationServiceApplication.exception.Reason;
import com.numeryx.AuthorizationServiceApplication.exception.UserException;
import com.numeryx.AuthorizationServiceApplication.exception.UsernameNotFoundException;
import com.numeryx.AuthorizationServiceApplication.mapper.MinifiedUserMapper;
import com.numeryx.AuthorizationServiceApplication.mapper.UserMapper;
import com.numeryx.AuthorizationServiceApplication.mapper.UserProfileResponseMapper;
import com.numeryx.AuthorizationServiceApplication.mapper.UserResponseMapper;
import com.numeryx.AuthorizationServiceApplication.model.Subscriber;
import com.numeryx.AuthorizationServiceApplication.model.SubscriberAccount;
import com.numeryx.AuthorizationServiceApplication.model.User;
import com.numeryx.AuthorizationServiceApplication.repository.TokenRepository;
import com.numeryx.AuthorizationServiceApplication.repository.UserRepository;
import com.numeryx.AuthorizationServiceApplication.service.IUserService;
import com.numeryx.AuthorizationServiceApplication.utilities.Constant;
import com.numeryx.AuthorizationServiceApplication.validator.impl.UserValidatorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.numeryx.AuthorizationServiceApplication.exception.UserException.ReasonCode.USER_NOT_FOUND;

@Service("userComponent")
public class UserServiceImpl extends UserAccountManagementImpl implements IUserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserMapper userMapper;
    private final UserResponseMapper userResponseMapper;
    private final MinifiedUserMapper minifiedUserMapper;
    private final UserValidatorImpl userValidator;
    private final UserProfileResponseMapper userProfileResponseMapper;
    private final NotificationFeignClient notificationFeignClient;
    private final TokenDataWrapper tokenDataWrapper;



    public UserServiceImpl(UserRepository userRepository, LoginAttemptService loginAttemptService, UserMapper userMapper, TokenRepository tokenRepository, NotificationFeignClient notificationFeignClient, UserResponseMapper userResponseMapper, MinifiedUserMapper minifiedUserMapper, UserValidatorImpl userValidator, UserProfileResponseMapper userProfileResponseMapper, NotificationFeignClient notificationFeignClient1, TokenDataWrapper tokenDataWrapper) {
        super(userRepository, loginAttemptService, tokenRepository, notificationFeignClient);
        this.userMapper = userMapper;
        this.userResponseMapper = userResponseMapper;
        this.minifiedUserMapper = minifiedUserMapper;
        this.userValidator = userValidator;
        this.userProfileResponseMapper = userProfileResponseMapper;
        this.notificationFeignClient = notificationFeignClient1;
        this.tokenDataWrapper = tokenDataWrapper;
    }

    @Override
    public UserDto create(CreateUserRequest createUserRequest) {
        logger.debug("Request to save users {}", createUserRequest);
        userValidator.beforeSave(createUserRequest);
        User user = userMapper.toEntity(createUserRequest);
        user.setEnabled(false);
        user.setAttempts(0);
        user.setUsername(user.getUsername().toLowerCase());
        user.setRole(RoleEnum.ROLE_SUPER_ADMIN);
        user = userRepository.save(user);
        sendUserSmsAndMailNotification(user,null);

        return userResponseMapper.toDto(user);
    }

    @Override
    public UserDto update(CreateUserRequest createUserRequest) {
        return null;
    }

    @Override
    public UserDto findById(Long id) {
        return null;
    }

    @Override
    public Page<UserDto> findAll(String searchValue, Pageable pageable) {
        return null;
    }

    @Override
    public List<UserDto> findAll() {
        return null;
    }

    @Override
    public void delete(Long id) {

    }


    @Override
    public UserDto getMe() {
        User userChecked = userRepository.findByUsernameIgnoreCase(
                SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if (userChecked == null) {
            throw new UserException(Collections.singletonList(new Reason(USER_NOT_FOUND, Constant.USER_NOT_FOUND_EXCEPTION)));
        }
        if (userChecked.getAttempts() != 0) {
            userChecked.setAttempts(0);
            userChecked.setEnabled(true);
            return userResponseMapper.toDto(userRepository.save(userChecked));
        } else {
            return userResponseMapper.toDto(userChecked);
        }
    }

    @Override
    public List<MinifiedUserDTO> getUserByUserName(List<String> userNames) {
        List<User> users = new ArrayList<>();
        for (String name : userNames){
            User userChecked = userRepository.findByUsernameIgnoreCase(name);
            if (userChecked != null) {
                users.add(userChecked);
            }
        }
        return minifiedUserMapper.toDto(users);
    }

    @Override
    public List<MinifiedUserDTO> getUserByIds(List<Long> ids) {
        List<User> users = userRepository.getUsersByIdIsIn(ids);
        return minifiedUserMapper.toDto(users);
    }

    @Override
    public List<MinifiedUserDTO> getAllProjectDirector() {
        logger.debug("Request to get logged all project director '{}'");
        List<MinifiedUserDTO> projectDirectors = minifiedUserMapper.toDto(userRepository.getAllProjectDirectorUserNames(RoleEnum.ROLE_ADMIN_PROJECT_DIRECTOR.ordinal()));
        logger.debug("List Project Director returned '{}'",projectDirectors);
        return projectDirectors;
    }

    @Override
    public List<MinifiedUserDTO> getAllFinancier() {
        logger.debug("Request to get logged all financier '{}'");
        List<MinifiedUserDTO> financier = minifiedUserMapper.toDto(userRepository.getAllFinancierUserNames(RoleEnum.ROLE_ADMIN_FINANCIER.ordinal()));
        logger.debug("List Financier returned '{}'",financier);
        return financier;
    }

    @Override
    public UserDto updateProfile(UpdateUserProfileRequest updateUserProfileRequest) {
        logger.debug("Request to update logged in user profile '{}'", updateUserProfileRequest);
        userValidator.beforeUpdateProfile(updateUserProfileRequest);
        Map<String, Object> tokenData = tokenDataWrapper.getDetailsFromToken();
        Long userIdFromToken = new Long((Integer) tokenData.get("userId"));;
        if(!userIdFromToken.equals(updateUserProfileRequest.getId())){
            throw new UserException(Arrays.asList(new Reason(UserException.ReasonCode.UNAUTHORIZED_ACTION, Constant.UNAUTHORIZED_ACTION_EXCEPTION)));
        }
        User loggedUser = userRepository.findByIdAndActiveTrue(userIdFromToken);
        if (loggedUser == null) {
            throw new UserException(Collections.singletonList(new Reason(USER_NOT_FOUND, Constant.USER_NOT_FOUND_EXCEPTION)));
        }
        loggedUser.buildUser(userProfileResponseMapper.toEntity(updateUserProfileRequest));
        return userResponseMapper.toDto(userRepository.save(loggedUser));
    }

    @Override
    public UserDto changeUserPassword(ChangeUserConfidentiality changeUserConfidentiality) {
        logger.debug("Request to change User Password : {}", changeUserConfidentiality);
        Map<String, Object> tokenData = tokenDataWrapper.getDetailsFromToken();
        Long userIdFromToken = new Long((Integer) tokenData.get("userId"));
        User userToUpdate = userRepository.getOne(userIdFromToken);
        if(!userIdFromToken.equals(changeUserConfidentiality.getId())){
            throw new UserException(Arrays.asList(new Reason(UserException.ReasonCode.UNAUTHORIZED_ACTION, Constant.UNAUTHORIZED_ACTION_EXCEPTION)));
        }
        if (userToUpdate == null) {
            throw new UsernameNotFoundException("User not found", Arrays.asList((new Reason(UsernameNotFoundException.ReasonCode.USER_NOT_FOUND, Constant.USER_NOT_FOUND_EXCEPTION))));
        }
        User userLogged = userRepository.findByUsernameIgnoreCase(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        String password = userLogged.getPassword();
        if (!userLogged.getId().equals(changeUserConfidentiality.getId()) && !userLogged.getRole().equals(RoleEnum.ROLE_SUPER_ADMIN)) {
            throw new UserException(Arrays.asList(new Reason(UserException.ReasonCode.UNAUTHORIZED_ACTION, Constant.UNAUTHORIZED_ACTION_EXCEPTION)));
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(changeUserConfidentiality.getPassword(), password)) {
            throw new UserException(Arrays.asList(new Reason(UserException.ReasonCode.WRONG_PASSWORD, Constant.WRONG_PASSWORD_EXCEPTION)));
        }
        if (!changeUserConfidentiality.getNewPassword().matches(Constant.simpleUserPasswordRegex)) {
            throw new UserException(Arrays.asList(new Reason(UserException.ReasonCode.WEAK_PASSWORD, Constant.WEAK_PASSWORD_EXCEPTION)));
        }
        userToUpdate.setPassword(passwordEncoder.encode(changeUserConfidentiality.getNewPassword()));
        User user = userRepository.save(userToUpdate);
        sendChangePasswordMethod(userToUpdate, null);
        return userResponseMapper.toDto(user);
    }

    @Async
    public void sendChangePasswordMethod(User entity, String token){
        ResetTokenRequest changePasswordRequest = setMailRequest(entity, token, false);
        this.notificationFeignClient.sendChangePasswordInfoEmail(changePasswordRequest);
    }

    @Override
    public Long getSubscriberId(Long id) {
//        SubscriberAccount user = (SubscriberAccount)userRepository.findFirstByIdAndRoleEquals(id, RoleEnum.ROLE_ADMIN_SUBSCRIBER);
        SubscriberAccount user = (SubscriberAccount)userRepository.findFirstById(id);
        if (user != null) {
            return user.getSubscriber().getId();
        } else {
            throw new UnauthorizedUserException("Unauthorized user");
        }
    }

}
