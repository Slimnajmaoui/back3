package com.numeryx.AuthorizationServiceApplication.mapper;

import com.numeryx.AuthorizationServiceApplication.dto.AddressDto;
import com.numeryx.AuthorizationServiceApplication.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, uses = {})
public interface AddressMapper extends EntityMapper<AddressDto, Address> {
}
