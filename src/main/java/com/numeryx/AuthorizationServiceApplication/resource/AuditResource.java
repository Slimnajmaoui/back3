package com.numeryx.AuthorizationServiceApplication.resource;

import com.numeryx.AuthorizationServiceApplication.dto.connection.ConnectionAuditDto;
import com.numeryx.AuthorizationServiceApplication.resource.util.Constants;
import com.numeryx.AuthorizationServiceApplication.service.ConnectionAuditService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/cnx-audit")
public class AuditResource {

    private final ConnectionAuditService cnxAuditService;

    public AuditResource(ConnectionAuditService cnxAuditService) {
        this.cnxAuditService = cnxAuditService;
    }

    @PreAuthorize(Constants.HAS_ROLE_SUPER_ADMIN)
    @GetMapping("/connections")
    @ApiOperation(value = "Find all connection events", response = ResponseEntity.class)
    public ResponseEntity<Page<ConnectionAuditDto>> findAll(@ApiParam Pageable pageable,
                                                            @RequestParam(required = false)
                                                            @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date,
                                                            @RequestParam(required = false) String principal) {
        return ResponseEntity.ok(cnxAuditService.findAll(pageable, date, principal));
    }
}
