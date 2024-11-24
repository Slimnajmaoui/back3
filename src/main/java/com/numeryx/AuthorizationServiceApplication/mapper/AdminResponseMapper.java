package com.numeryx.AuthorizationServiceApplication.mapper;

import com.numeryx.AuthorizationServiceApplication.dto.AdminDTO;
import com.numeryx.AuthorizationServiceApplication.model.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, uses = {AddressMapper.class})
public interface AdminResponseMapper extends EntityMapper<AdminDTO, Admin> {
}
