package com.numeryx.AuthorizationServiceApplication.repository;

import com.numeryx.AuthorizationServiceApplication.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProviderRepository extends JpaRepository<Provider, Long> {
}
