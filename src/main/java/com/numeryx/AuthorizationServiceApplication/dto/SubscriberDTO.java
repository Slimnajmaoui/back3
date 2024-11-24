package com.numeryx.AuthorizationServiceApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriberDTO extends AbstractDto {
    private String subscriberName;
    private String commercialDesignation;
    private String siret;
    private String codeNAF;
    private AddressDto address;
    private boolean hasAccount = false;
    private String utilisateurMail;
}
