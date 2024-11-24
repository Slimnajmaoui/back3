package com.numeryx.AuthorizationServiceApplication.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "createdDate")
    private Date createdDate = new Date();

    @Column(name = "updatedDate")
    private Date updatedDate = new Date();

    @Column(name = "active")
    private boolean active;

    @Column(name = "createdBy")
    private String createdBy;

    @PrePersist
    void onCreate() {
        if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            this.setCreatedBy((String) user);
        }
        this.setCreatedDate(new Date());
        this.setUpdatedDate(new Date());
        this.setActive(true);
    }

    @PreUpdate
    void onUpdate() {
        this.setUpdatedDate(new Date());
    }

}
