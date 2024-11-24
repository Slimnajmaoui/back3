package com.numeryx.AuthorizationServiceApplication.config.audit;

import com.numeryx.AuthorizationServiceApplication.model.ConnectionAudit;
import com.numeryx.AuthorizationServiceApplication.repository.ConnectionAuditRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomAuditEventRepository implements AuditEventRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuditEventRepository.class);

    @Autowired
    private AuditEventConverter auditEventConverter;

    @Autowired
    private ConnectionAuditRepository connectionAuditRepository;

    @Override
    public List<AuditEvent> find(String principal, Instant after, String type) {
        Iterable<ConnectionAudit> persistentAuditEvents;
        if (principal == null && after == null) {
            persistentAuditEvents = connectionAuditRepository.findAll();
        } else if (after == null) {
            persistentAuditEvents = connectionAuditRepository.findByPrincipal(principal);
        } else {
            persistentAuditEvents =
                    connectionAuditRepository.findByPrincipalAndAuditEventDateAfter(principal, after
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime(), type);
        }
        return auditEventConverter.convertToAuditEvent(persistentAuditEvents);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void add(AuditEvent event) {
        try {
            Map<String, Object> data = new HashMap<>(event.getData());
            if (RequestContextHolder.getRequestAttributes() != null) {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                LOGGER.debug("New connection for real ip user {} and all headers names are {}", request.getHeader("X-Forwarded-For"), request.getHeaderNames());
                if (request.getHeader("X-Forwarded-For") != null) {
                    data.put("real-ip", request.getHeader("X-Forwarded-For"));
                }
            }
            if (event.getPrincipal() != null) {
                data.put("username", event.getPrincipal());
            }
            LOGGER.debug("New audit event. Event is {} and new data is {}", event, data);
            ConnectionAudit persistentAuditEvent = new ConnectionAudit();
            persistentAuditEvent.setPrincipal(event.getPrincipal());
            persistentAuditEvent.setAuditEventType(event.getType());
            persistentAuditEvent.setAuditEventDate(LocalDateTime.ofInstant(event.getTimestamp(), ZoneId.systemDefault()));
            persistentAuditEvent.setData(auditEventConverter.convertDataToStrings(data));
            connectionAuditRepository.save(persistentAuditEvent);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.debug("Error while processing new entry on audit. Details {} and cause is {}", e.getMessage(), e.getCause());
        }
    }

}
