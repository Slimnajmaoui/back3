package com.numeryx.AuthorizationServiceApplication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    @Qualifier("userService")
    private UserDetailsService userDetailsService;

    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;

    @Value("${security.oauth2.client.access-token-validity-seconds}")
    private int accessTokenTimeout;

    @Value("${security.oauth2.client.refresh-token-validity-seconds}")
    private int refreshTokenTimeout;

    @Value("${security.oauth2.client.client-id}")
    private String client;

    @Value("${security.oauth2.client.client-secret}")
    private String secret;

    @Value("${security.jwt.key-store-password}")
    private String keyStorePassword;

    @Value("${security.jwt.key-pair-alias}")
    private String keyStoreAlias;

    @Value("${config.path-jks}")
    private String pathJks;

    @Value("${security.jwt.signing-key}")
    private String signingKey;

    @Autowired
    @Qualifier("clientDetailsService")
    private ClientDetailsService clientDetailsService;

    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomTokenEnhancer customTokenEnhancer;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(client)
                .secret(passwordEncoder.encode(secret))
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("trust")
                .resourceIds("authorization-service", "notification-service", "rc-control-service", "minio-ged-service");

    }

    @Bean("accessTokenConverter")
    public JwtAccessTokenConverter accessTokenConverter() {

        if (jwtAccessTokenConverter != null) {
            return jwtAccessTokenConverter;
        }
        jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setAccessTokenConverter(new CustomAccessTokenConverter());
       // jwtAccessTokenConverter.setSigningKey(signingKey);
        Resource ksFile = new FileSystemResource(pathJks);
        KeyStoreKeyFactory ksFactory =
                new KeyStoreKeyFactory(ksFile, keyStorePassword.toCharArray());
        KeyPair keyPair = ksFactory.getKeyPair(keyStoreAlias);
        jwtAccessTokenConverter.setKeyPair(keyPair);

        return jwtAccessTokenConverter;
    }

    @Bean("tokenStore")
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean("tokenServices")
    @Primary
    public DefaultTokenServices tokenServices() {

        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(customTokenEnhancer, accessTokenConverter()));
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setClientDetailsService(clientDetailsService);
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setRefreshTokenValiditySeconds(refreshTokenTimeout);
        defaultTokenServices.setAccessTokenValiditySeconds(accessTokenTimeout);
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);
        return defaultTokenServices;
    }

    /**
     * Defines the authorization and token endpoints and the token services
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(customTokenEnhancer, accessTokenConverter()));
        endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS)
                .tokenStore(tokenStore())
                .accessTokenConverter(accessTokenConverter())
                .tokenEnhancer(tokenEnhancerChain)
                .tokenServices(tokenServices());

    }

}
