package com.numeryx.AuthorizationServiceApplication.model;


import com.numeryx.AuthorizationServiceApplication.enumeration.FunctionEnum;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("admin")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class Admin extends User{

    @Column(name = "function")
    @Enumerated(EnumType.STRING)
    private FunctionEnum function;

}
