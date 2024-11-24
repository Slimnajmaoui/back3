package com.numeryx.AuthorizationServiceApplication.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetActivationCodeSMSRequest implements Serializable {
    private String identifiant;
    private Long id;
    private String phone;
    private String message;
}
