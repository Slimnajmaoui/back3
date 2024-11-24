package com.numeryx.AuthorizationServiceApplication.dto.connection;

import com.numeryx.AuthorizationServiceApplication.model.ConnectionAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConnectionAuditDto {

    private Long id;
    private String principal;
    private LocalDateTime auditEventDate;
    private String auditEventType;
    private Map<String, String> data;

    public static ConnectionAuditDto formEntity(ConnectionAudit cnx) {
        ConnectionAuditDto dto = ConnectionAuditDto.builder()
                .id(cnx.getId())
                .principal(cnx.getPrincipal())
                .auditEventDate(cnx.getAuditEventDate())
                .auditEventType(cnx.getAuditEventType())
                .data(cnx.getData())
                .build();
        return dto;
    }
}
