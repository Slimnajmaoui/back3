package com.numeryx.AuthorizationServiceApplication.dto;

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
public class AdminDTO extends UserDto {

    private FunctionEnum function;
}
