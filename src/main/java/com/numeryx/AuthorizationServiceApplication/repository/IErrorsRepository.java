package com.numeryx.AuthorizationServiceApplication.repository;

import com.numeryx.AuthorizationServiceApplication.model.Errors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IErrorsRepository extends JpaRepository<Errors, Long> {
}
