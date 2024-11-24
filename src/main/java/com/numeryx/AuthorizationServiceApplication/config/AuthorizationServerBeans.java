package com.numeryx.AuthorizationServiceApplication.config;

import com.numeryx.AuthorizationServiceApplication.security.SystemTokenAdapter;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import static com.numeryx.AuthorizationServiceApplication.utilities.Methods.getJWTToken;

@Configuration
@ComponentScan("com.numeryx.AuthorizationServiceApplication")
@EnableJpaRepositories("com.numeryx.AuthorizationServiceApplication.repository")
public class AuthorizationServerBeans {

    @Autowired
    private SystemTokenAdapter systemTokenAdapter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            try {
                requestTemplate.header("Authorization", getJWTToken());
            } catch (UnauthorizedUserException e) {
                e.printStackTrace();
                requestTemplate.header("Authorization", "Bearer " + systemTokenAdapter.generateSystemToken());
            }
        };
    }
}
