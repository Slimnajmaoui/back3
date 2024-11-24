package com.numeryx.AuthorizationServiceApplication.mapper;


import com.numeryx.AuthorizationServiceApplication.dto.PartnerAccountDTO;
import com.numeryx.AuthorizationServiceApplication.dto.UserDto;
import com.numeryx.AuthorizationServiceApplication.model.PartnerAccount;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, uses = {UserDto.class})

public interface PartnerAccountMapper extends EntityMapper<PartnerAccountDTO, PartnerAccount> {
}
