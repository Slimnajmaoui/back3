package com.numeryx.AuthorizationServiceApplication.config;

import com.numeryx.AuthorizationServiceApplication.dto.UserDetailsDTO;
import com.numeryx.AuthorizationServiceApplication.enumeration.RoleEnum;
import com.numeryx.AuthorizationServiceApplication.model.SubscriberAccount;
import com.numeryx.AuthorizationServiceApplication.model.User;
import com.numeryx.AuthorizationServiceApplication.repository.ISubscriberAccountRepository;
import com.numeryx.AuthorizationServiceApplication.repository.ISubscriberRepository;
import com.numeryx.AuthorizationServiceApplication.repository.UserRepository;
import com.numeryx.AuthorizationServiceApplication.service.impl.LoginAttemptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CustomTokenEnhancer implements TokenEnhancer {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ISubscriberRepository subscriberRepository;

    private final Logger logger = LoggerFactory.getLogger(CustomTokenEnhancer.class);

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        UserDetailsDTO user;
        if (oAuth2Authentication.getPrincipal() instanceof UserDetailsDTO) {
            user = ((UserDetailsDTO) oAuth2Authentication.getPrincipal());
        } else if (oAuth2Authentication.getPrincipal() instanceof String) {
            String username = (String) oAuth2Authentication.getPrincipal();
            user = userRepository
                    .findDetailsByUsernameIgnoreCase(username);
        } else {
            logger.debug("Principal could not be cast to either String or UserDetailsDTO. Details: {}", oAuth2Authentication);
            throw new UnauthorizedUserException("Unauthorized user");
        }
        if (user != null) {
            Map<String, Object> additionalInfo = new HashMap<>();
           additionalInfo.put("userId", user.getId());
           additionalInfo.put("role", user.getRole());
           if(user.getRole().equals(RoleEnum.ROLE_USER_SUBSCRIBER) ||
                   user.getRole().equals(RoleEnum.ROLE_ADMIN_SUBSCRIBER)) {
               List<Long> subscriberIds = this.subscriberRepository
                                                .findSubscribersIdByUserId(
                                                        user.getId()
                                                );
               additionalInfo.put("listLongSubscriberIds", subscriberIds);
           }
            ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionalInfo);
        }
        return oAuth2AccessToken;
    }
}
