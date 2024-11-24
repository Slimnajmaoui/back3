package com.numeryx.AuthorizationServiceApplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "connection_audit")
@Data
@NoArgsConstructor
@AllArgsConstructor
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class ConnectionAudit implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "event_id")
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String principal;

    @Column(name = "event_date")
    private LocalDateTime auditEventDate;

    @Column(name = "event_type")
    private String auditEventType;

    @ElementCollection
    @MapKeyColumn(name = "name")
    @Column(name = "value", length = 500000)
    @CollectionTable(name = "T_PERSISTENT_AUDIT_EVENT_DATA", joinColumns = @JoinColumn(name = "event_id"))
    private Map<String, String> data = new HashMap<>();
}
