package com.numeryx.AuthorizationServiceApplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscriber extends AbstractEntity {

    private String subscriberName;
    private String commercialDesignation;
    private String siret;
    private String codeNAF;
    @OneToOne
    private Address address;
    private boolean hasAccount = false;

}
