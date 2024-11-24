package com.numeryx.AuthorizationServiceApplication.service;

import com.numeryx.AuthorizationServiceApplication.dto.connection.ConnectionAuditDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ConnectionAuditService {
    Page<ConnectionAuditDto> findAll(Pageable pageable, LocalDate date, String principal);

}
