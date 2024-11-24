package com.numeryx.AuthorizationServiceApplication.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MinifiedUserDTO {
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String job;
}
