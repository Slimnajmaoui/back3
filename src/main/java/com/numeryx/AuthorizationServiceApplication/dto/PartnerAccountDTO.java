package com.numeryx.AuthorizationServiceApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerAccountDTO extends UserDto {
    private List<PartnerAccountDTO> delegates;

}
