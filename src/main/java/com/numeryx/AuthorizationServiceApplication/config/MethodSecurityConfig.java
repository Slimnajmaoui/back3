package com.numeryx.AuthorizationServiceApplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
//enable Expression-Based Access Control by setting prePostEnabled to true to use Spring Expression Language (SpEL)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {


    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return defaultMethodSecurityExpressionHandler();
    }

    @Bean
    DefaultMethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler() {
        return new DefaultMethodSecurityExpressionHandler();
    }

    @Bean("authenticationManager")
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
