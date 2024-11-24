package com.numeryx.AuthorizationServiceApplication.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;


@Configuration
@EnableResourceServer
@Slf4j
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    //private static final Logger LOGGER = Logger.getLogger(ResourceServerConfiguration.class);

    @Value("${security.oauth2.resource.id}")
    private String resourceId;

    private static final String[] PUBLIC_MATCHERS = {
            "/oauth/token",
            "/forgetpassword/**",
            "**/forgetpassword/**",
            "/swagger-ui.html#/",
            "/open-api/**"

    };
    private static final String[] PRIVATE_MATCHERS = {
            "/api/**"
    };

    @Autowired
    @Qualifier("tokenStore")
    private TokenStore tokenStore;

    @Autowired
    @Qualifier("tokenServices")
    private DefaultTokenServices tokenServices;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(PUBLIC_MATCHERS).permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers(PRIVATE_MATCHERS).authenticated();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources
                .resourceId(resourceId)
                .tokenServices(tokenServices)
                .tokenStore(tokenStore);
    }


}
