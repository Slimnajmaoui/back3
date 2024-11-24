package com.numeryx.AuthorizationServiceApplication.config.authentification;

import com.numeryx.AuthorizationServiceApplication.service.impl.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureEventListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    @Autowired
    private LoginAttemptService loginAttemptService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
        if (e.getException() != null &&
                e.getException().getMessage() != null &&
                e.getException().getMessage().startsWith("Access token expired")) {
            return;
        }
        String username = (String) e.getAuthentication().getPrincipal();
        loginAttemptService.loginFailed(username);
    }
}
