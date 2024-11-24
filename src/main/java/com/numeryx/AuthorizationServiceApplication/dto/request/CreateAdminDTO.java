package com.numeryx.AuthorizationServiceApplication.dto.request;

import com.numeryx.AuthorizationServiceApplication.dto.BaseUserDto;
import com.numeryx.AuthorizationServiceApplication.enumeration.FunctionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdminDTO extends CreateUserRequest {

    private FunctionEnum function;
}
