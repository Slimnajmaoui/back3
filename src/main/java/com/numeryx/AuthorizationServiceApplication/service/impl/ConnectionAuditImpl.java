package com.numeryx.AuthorizationServiceApplication.service.impl;

import com.numeryx.AuthorizationServiceApplication.dto.connection.ConnectionAuditDto;
import com.numeryx.AuthorizationServiceApplication.repository.ConnectionAuditRepository;
import com.numeryx.AuthorizationServiceApplication.service.ConnectionAuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class ConnectionAuditImpl implements ConnectionAuditService {

    private final ConnectionAuditRepository cnxAuditRepository;

    private final Logger logger = LoggerFactory.getLogger(ConnectionAuditImpl.class);


    public ConnectionAuditImpl(ConnectionAuditRepository cnxAuditRepository) {
        this.cnxAuditRepository = cnxAuditRepository;
    }

    public Page<ConnectionAuditDto> findAll(Pageable pageable, LocalDate date, String principal) {
        logger.debug("Fetch data for connection audit with filter: {} {} {}", pageable, date, principal);
        if (principal != null && date != null) {
            LocalDateTime castedDate = LocalDateTime.of(date, LocalTime.MIN);
            return cnxAuditRepository.findByAuditEventDateAfterAndPrincipal(castedDate, principal, pageable)
                    .map(ConnectionAuditDto::formEntity);
        }

        if (principal != null) {
            return cnxAuditRepository.findByPrincipal(principal, pageable)
                    .map(ConnectionAuditDto::formEntity);
        }

        if (date != null) {
            LocalDateTime castedDate = LocalDateTime.of(date, LocalTime.MIN);
            return cnxAuditRepository.findByAuditEventDateAfter(castedDate, pageable)
                    .map(ConnectionAuditDto::formEntity);
        }

        return cnxAuditRepository.findAll(pageable)
                .map(ConnectionAuditDto::formEntity);
    }
}
