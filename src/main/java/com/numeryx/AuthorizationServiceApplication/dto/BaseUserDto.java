package com.numeryx.AuthorizationServiceApplication.dto;

import com.numeryx.AuthorizationServiceApplication.enumeration.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseUserDto extends AbstractDto implements Serializable {

    private String username;

    private String firstname;

    private String lastname;

    private String job;

    private String phone;

    private String phoneChange;

    private String usernameChange;

    private boolean enabled = true;

    private int attempts;

    private boolean nonLocked = true;

    private RoleEnum role;
}
