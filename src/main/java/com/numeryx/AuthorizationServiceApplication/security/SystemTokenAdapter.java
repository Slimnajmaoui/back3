package com.numeryx.AuthorizationServiceApplication.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.Signer;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.Date;

@Component
public class SystemTokenAdapter {

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.jwt.key-store-password}")
    private String keyStorePassword;

    @Value("${security.jwt.key-pair-alias}")
    private String keyStoreAlias;

    @Value("${config.path-jks}")
    private String pathJks;

    @Value("${security.jwt.signing-key}")
    private String signingKey;
    private final Logger logger = LoggerFactory.getLogger(SystemTokenAdapter.class);

    public String generateSystemToken() {
        Date validity = new Date(
                (new Date()).getTime() + 1000000);
        Signer signer;
        try {
            Resource ksFile = new FileSystemResource(pathJks);
            KeyStoreKeyFactory ksFactory =
                    new KeyStoreKeyFactory(ksFile, keyStorePassword.toCharArray());
            KeyPair keyPair = ksFactory.getKeyPair(keyStoreAlias);
            signer = new RsaSigner((RSAPrivateKey) keyPair.getPrivate());
        //    signer = new MacSigner(signingKey);
        } catch (Exception ex) {
            logger.debug("Exception while processing pem file {} and cause is {}", ex.getMessage(), ex.getCause());
            signer = new RsaSigner("NUMERYXTESTKEY");
        }
        CharSequence content = ""
                + "{\"clientId\":\"" + clientId + "\","
                + "\"user_name\":\"system\","
                + "\"scope\":[\"trust\"],"
                + "\"aud\":[\"payment-service\", \"authorization-service\", \"notification-service\"],"
                + "\"exp\":" + validity.getTime() + ","
                + "\"authorities\":[\"ROLE_SUPER_ADMIN\"],"
                + "\"jti\":\"35dfaa4c-0e30-4869-9a94-381d3ecf4f50\","
                + "\"client_id\":\"" + clientId + "\"}";
        return JwtHelper.encode(content, signer).getEncoded();
    }
}
