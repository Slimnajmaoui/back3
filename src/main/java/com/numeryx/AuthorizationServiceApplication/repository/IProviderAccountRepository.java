package com.numeryx.AuthorizationServiceApplication.repository;

import com.numeryx.AuthorizationServiceApplication.model.ProviderAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProviderAccountRepository extends JpaRepository<ProviderAccount, Long> {
}
