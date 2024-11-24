package com.numeryx.AuthorizationServiceApplication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProviderDTO {

    private String providerName;
}
