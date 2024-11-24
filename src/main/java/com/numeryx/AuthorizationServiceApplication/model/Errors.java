package com.numeryx.AuthorizationServiceApplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "errors")
@Data
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class Errors extends AbstractEntity implements Serializable {

    @Column(name = "message")
    private String message;

    @Column(name = "status")
    private int status;

    @Column(name = "errorCode")
    private String errorCode;

    @Column(name = "reason")
    @Size(max = 1000)
    private String errors;
}
