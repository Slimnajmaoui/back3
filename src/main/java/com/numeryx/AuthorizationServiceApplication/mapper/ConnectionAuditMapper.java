package com.numeryx.AuthorizationServiceApplication.mapper;

import com.numeryx.AuthorizationServiceApplication.dto.connection.ConnectionAuditDto;
import com.numeryx.AuthorizationServiceApplication.model.ConnectionAudit;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, uses = {})
public interface ConnectionAuditMapper extends EntityMapper<ConnectionAuditDto, ConnectionAudit> {
}
