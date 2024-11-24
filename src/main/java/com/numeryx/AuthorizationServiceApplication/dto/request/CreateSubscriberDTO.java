package com.numeryx.AuthorizationServiceApplication.dto.request;

import com.numeryx.AuthorizationServiceApplication.dto.AddressDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubscriberDTO implements Serializable {

    private String subscriberName;
    private String commercialDesignation;
    private String siret;
    private String codeNAF;
    private AddressDto address;

}
