package com.numeryx.AuthorizationServiceApplication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TokenDataWrapper {

    @Autowired
    private TokenStore tokenStore;

    /**
     * get connected user information from token
     *
     * @return Map of user details
     */
    public Map<String, Object> getDetailsFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
            OAuth2AccessToken token = tokenStore.readAccessToken(oAuth2AuthenticationDetails.getTokenValue());
            return token.getAdditionalInformation();
        }
        return null;
    }
}
