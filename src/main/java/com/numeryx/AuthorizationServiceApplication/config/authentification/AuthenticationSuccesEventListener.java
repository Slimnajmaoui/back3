package com.numeryx.AuthorizationServiceApplication.config.authentification;

import com.numeryx.AuthorizationServiceApplication.enumeration.RoleEnum;
import com.numeryx.AuthorizationServiceApplication.security.EntryPointSecurityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccesEventListener implements ApplicationListener<AuthenticationSuccessEvent> {
    @Autowired
    private EntryPointSecurityHandler entryPointSecurityHandler;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent e) {
        if(e.getAuthentication().getAuthorities().size() != 0){
            RoleEnum role = RoleEnum.valueOf(
                    e.getAuthentication()
                            .getAuthorities()
                            .toArray()[0]
                            .toString()
            );
            this.entryPointSecurityHandler.checkHeader(role);
        }
    }
}
