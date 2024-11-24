package com.numeryx.AuthorizationServiceApplication.dto.request;

import com.numeryx.AuthorizationServiceApplication.dto.BaseUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;



@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest extends BaseUserDto {

    private String password;
}
