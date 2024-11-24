package com.numeryx.AuthorizationServiceApplication.model;


import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("provider")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class ProviderAccount extends  User {

    @OneToOne
    @JoinColumn(name = "admin_account_id")
    private ProviderAccount adminAccount;

    @ManyToOne
    @JoinColumn(name = "provider")
    private Provider provider;

}
