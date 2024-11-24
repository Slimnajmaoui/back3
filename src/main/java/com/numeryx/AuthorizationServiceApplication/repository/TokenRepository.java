package com.numeryx.AuthorizationServiceApplication.repository;

import com.numeryx.AuthorizationServiceApplication.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    Token findTokenByActivationCodeAndIdUser_Id(String activationCode, Long id);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    Token findTokenByMailTokenAndIdUser_Id(String token, Long id);
}
