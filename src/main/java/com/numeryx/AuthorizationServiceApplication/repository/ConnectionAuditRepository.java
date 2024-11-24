package com.numeryx.AuthorizationServiceApplication.repository;

import com.numeryx.AuthorizationServiceApplication.model.ConnectionAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConnectionAuditRepository extends JpaRepository<ConnectionAudit, String> {

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    Page<ConnectionAudit> findByPrincipal(String principal, Pageable pageable);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    List<ConnectionAudit> findByPrincipal(String principal);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    Page<ConnectionAudit> findByAuditEventDateAfter(LocalDateTime date, Pageable pageable);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    Page<ConnectionAudit> findByAuditEventDateAfterAndPrincipal(LocalDateTime date, String principal, Pageable pageable);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    @Query("select connectionAudit from ConnectionAudit connectionAudit where connectionAudit.principal = ?1 and connectionAudit.auditEventDate >= ?2 and connectionAudit.auditEventType = ?3")
    List<ConnectionAudit> findByPrincipalAndAuditEventDateAfter(String principal, LocalDateTime toDate, String type);
}
