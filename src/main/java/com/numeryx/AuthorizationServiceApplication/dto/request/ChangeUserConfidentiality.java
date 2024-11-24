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
public class ChangeUserConfidentiality implements Serializable {
    private Long id;
    private String password;
    private String newPassword;
    private String newPhone;
    private String username;
    private String newUsername;
    private String activationCode;
    private String token;
}
