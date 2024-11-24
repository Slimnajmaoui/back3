package com.numeryx.AuthorizationServiceApplication.mapper;

import com.numeryx.AuthorizationServiceApplication.dto.AddressDto;
import com.numeryx.AuthorizationServiceApplication.dto.TacheDto;
import com.numeryx.AuthorizationServiceApplication.model.Address;
import com.numeryx.AuthorizationServiceApplication.model.Tache;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, uses = {})
public interface TacheMapper extends EntityMapper<TacheDto, Tache> {
}
