package com.numeryx.AuthorizationServiceApplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class Token implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "MAIL_TOKEN")
    private String mailToken;

    @Column(name = "ACTIVATION_CODE")
    private String activationCode;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User idUser;

    @Column(name = "MAIL_TOKEN_DATE")
    private Date mailTokenCreationDate;

    @Column(name = "ACT_CODE_DATE")
    private Date activationCodeCreationDate;

    @Column(name = "ENABLED_TOKEN")
    private boolean enabledToken;

    @Column(name = "ENABLED_CODE")
    private boolean enabledCode;

    @Column(name = "NB_SEND_ACT_COD")
    private int nbSendActivationCodeAttempt;

    @Column(name = "NB_Fail_ATT_ACT_COD")
    private int nbFailedAttemptActivationCode;
}
