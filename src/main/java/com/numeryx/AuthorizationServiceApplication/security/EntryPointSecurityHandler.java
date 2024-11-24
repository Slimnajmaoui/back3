package com.numeryx.AuthorizationServiceApplication.security;

import com.numeryx.AuthorizationServiceApplication.enumeration.RoleEnum;
import com.numeryx.AuthorizationServiceApplication.exception.Reason;
import com.numeryx.AuthorizationServiceApplication.exception.UserException;
import com.numeryx.AuthorizationServiceApplication.model.User;
import com.numeryx.AuthorizationServiceApplication.service.impl.LoginAttemptService;
import com.numeryx.AuthorizationServiceApplication.utilities.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Component
public class EntryPointSecurityHandler {

    Logger logger = LoggerFactory.getLogger(EntryPointSecurityHandler.class);
    @Autowired
    private LoginAttemptService loginAttemptService;

    @Value("${url-backoffice}")
    private String contextBackOffice;

    @Value("${url-subscriber}")
    private String contextSubscriber;

    @Value("${url-partner}")
    private String contextPartner;

    public void checkHeader(RoleEnum userRole){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
        String url = request.getHeader("Referer");
        logger.debug("New request to check security for url {} and user role is {} and current authentication principal is {}",
                url, userRole.toString(),
                SecurityContextHolder.getContext() != null &&
                        SecurityContextHolder.getContext().getAuthentication() != null &&
                        SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null ?
                SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString() : "null");
        if(SecurityContextHolder.getContext() != null &&
        SecurityContextHolder.getContext().getAuthentication() != null &&
        SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            switch (userRole){
                case ROLE_SUPER_ADMIN:
                case ROLE_ADMIN_FINANCIER:
                case ROLE_ADMIN_PROJECT_DIRECTOR:
                    if(url.equalsIgnoreCase(contextPartner) || url.equalsIgnoreCase(contextSubscriber)){
                        blockWhenLoginFailed();
                        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
                        SecurityContextHolder.clearContext();
                        throw new UserException(Arrays.asList(new Reason(UserException.ReasonCode.UNAUTHORIZED_ACTION, Constant.UNAUTHORIZED_ACTION_EXCEPTION)));
                    }
                    break;
                case ROLE_USER_PROVIDER:
                case ROLE_ADMIN_PROVIDER:
                    if(url.equalsIgnoreCase(contextBackOffice)) {
                        blockWhenLoginFailed();
                        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
                        SecurityContextHolder.clearContext();
                        throw new UserException(Arrays.asList(new Reason(UserException.ReasonCode.UNAUTHORIZED_ACTION, Constant.UNAUTHORIZED_ACTION_EXCEPTION)));
                    }
                    if(url.equalsIgnoreCase(contextSubscriber)) {
                        blockWhenLoginFailed();
                        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
                        SecurityContextHolder.clearContext();
                        throw new UserException(Arrays.asList(new Reason(UserException.ReasonCode.UNAUTHORIZED_ACTION, Constant.UNAUTHORIZED_ACTION_EXCEPTION)));
                    }
                    break;
                case ROLE_USER_SUBSCRIBER:
                case ROLE_ADMIN_SUBSCRIBER:
                    if(url.equalsIgnoreCase(contextBackOffice)) {
                        blockWhenLoginFailed();
                        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
                        SecurityContextHolder.clearContext();
                        throw new UserException(Arrays.asList(new Reason(UserException.ReasonCode.UNAUTHORIZED_ACTION, Constant.UNAUTHORIZED_ACTION_EXCEPTION)));
                    }
                    if(url.equalsIgnoreCase(contextPartner)) {
                        blockWhenLoginFailed();
                        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
                        SecurityContextHolder.clearContext();
                        throw new UserException(Arrays.asList(new Reason(UserException.ReasonCode.UNAUTHORIZED_ACTION, Constant.UNAUTHORIZED_ACTION_EXCEPTION)));
                    }
                    break;
            }
        }
    }

    private void blockWhenLoginFailed(){
        try{
            String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal()
                    instanceof User ?
                    ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername() :
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            logger.debug("Current user to block is {}", username);
            loginAttemptService.loginFailed(
                    username
            );
        }catch (Exception e){
            e.printStackTrace();
            logger.debug("Error while blocking user details {} and cause is {}", e.getMessage(), e.getCause());
        }
    }
}
