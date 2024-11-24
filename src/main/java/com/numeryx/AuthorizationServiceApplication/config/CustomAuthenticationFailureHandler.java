package com.numeryx.AuthorizationServiceApplication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private MessageSource messages;


    public void onAuthenticationFailure(RuntimeException e) {
        String errorMessage = messages.getMessage("message.badCredentials", null, Locale.FRENCH);
        if (e.getMessage().equalsIgnoreCase("blocked")) {
            errorMessage = messages.getMessage("auth.message.blocked", null, Locale.FRENCH);
        }
    }
}
