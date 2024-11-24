package com.numeryx.AuthorizationServiceApplication.service.impl;

import com.numeryx.AuthorizationServiceApplication.client.NotificationFeignClient;
import com.numeryx.AuthorizationServiceApplication.dto.request.ResetTokenRequest;
import com.numeryx.AuthorizationServiceApplication.model.Token;
import com.numeryx.AuthorizationServiceApplication.model.User;
import com.numeryx.AuthorizationServiceApplication.repository.TokenRepository;
import com.numeryx.AuthorizationServiceApplication.repository.UserRepository;
import com.numeryx.AuthorizationServiceApplication.service.IUserAccountManagement;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.UUID;

@Service
public class UserAccountManagementImpl implements IUserAccountManagement {

    protected final UserRepository userRepository;
    protected final LoginAttemptService loginAttemptService;
    private final TokenRepository tokenRepository;
    private final NotificationFeignClient notificationFeignClient;

    public UserAccountManagementImpl(UserRepository userRepository, LoginAttemptService loginAttemptService, TokenRepository tokenRepository, NotificationFeignClient notificationFeignClient) {
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
        this.tokenRepository = tokenRepository;
        this.notificationFeignClient = notificationFeignClient;
    }

    protected void sendUserSmsAndMailNotification(User savedEntity,String function) {
        String token = UUID.randomUUID().toString();
        String activationCode = RandomStringUtils.randomNumeric(4);
        Token tokenDB = new Token();
        tokenDB.setMailToken(token);
        tokenDB.setActivationCode(activationCode);
        tokenDB.setMailTokenCreationDate(new Date());
        tokenDB.setActivationCodeCreationDate(new Date());
        tokenDB.setIdUser(savedEntity);
        tokenDB.setEnabledToken(true);
        tokenDB.setEnabledCode(true);
        this.tokenRepository.save(tokenDB);
        sendRequestPasswordMethod(savedEntity, token, function);
        sendActivationCodeMethod(savedEntity, activationCode);
    }

    protected  void sendSubscriberSmsAndMailNotification(User savedEntity) {
        String token = UUID.randomUUID().toString();
        String activationCode = RandomStringUtils.randomNumeric(4);
        Token tokenDB = new Token();
        tokenDB.setMailToken(token);
        tokenDB.setActivationCode(activationCode);
        tokenDB.setMailTokenCreationDate(new Date());
        tokenDB.setActivationCodeCreationDate(new Date());
        tokenDB.setIdUser(savedEntity);
        tokenDB.setEnabledToken(true);
        tokenDB.setEnabledCode(true);
        this.tokenRepository.save(tokenDB);
        sendRequestPasswordMethodForSubscriber(savedEntity, token);
        sendActivationCodeMethod(savedEntity, activationCode);
    }

    @Async
    protected void sendRequestPasswordMethod(User entity, String token, String function){
        ResetTokenRequest resetPasswordRequest = setMailRequest(entity, token, false);
        this.notificationFeignClient.sendRequestPassword(resetPasswordRequest, function);
    }
    @Async
    protected void sendRequestPasswordMethodForSubscriber(User entity, String token){
        ResetTokenRequest resetPasswordRequest = setMailRequest(entity, token, false);
        this.notificationFeignClient.sendRequestPasswordSubscriber(resetPasswordRequest);
    }

    @Async
    protected void sendActivationCodeMethod(User entity, String token){
        ResetTokenRequest resetPasswordRequest = setMailRequest(entity, token, false);
        this.notificationFeignClient.sendActivationCode(resetPasswordRequest);
    }

}
