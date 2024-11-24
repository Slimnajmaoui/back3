package com.numeryx.AuthorizationServiceApplication.mapper;

import com.numeryx.AuthorizationServiceApplication.dto.request.CreateUserRequest;
import com.numeryx.AuthorizationServiceApplication.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, uses = {AddressMapper.class})
public interface UserMapper extends EntityMapper<CreateUserRequest, User> {
}
