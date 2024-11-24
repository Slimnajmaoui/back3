package com.numeryx.AuthorizationServiceApplication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ChangeUserCredentials implements Serializable {
    private Long id;
    private String token;
    private String password;
}
