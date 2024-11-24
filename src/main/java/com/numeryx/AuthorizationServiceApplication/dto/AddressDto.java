package com.numeryx.AuthorizationServiceApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private Long id;

    private String mainAddress;

    private String complementaryAddress;

    private Integer zipCode;

    private String city;
    
    private String name;
}
