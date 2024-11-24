package com.numeryx.AuthorizationServiceApplication.mapper;


import com.numeryx.AuthorizationServiceApplication.dto.MinifiedUserDTO;
import com.numeryx.AuthorizationServiceApplication.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, uses = {AddressMapper.class})
public interface MinifiedUserMapper extends EntityMapper<MinifiedUserDTO, User>{
}
