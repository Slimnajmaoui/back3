package com.numeryx.AuthorizationServiceApplication.model;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("partner")
public class PartnerAccount extends User {
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "partnerAccount")
    private List<PartnerAccount> delegates;
    @ManyToOne()
    private Admin partnerAccount;
}
