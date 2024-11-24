package com.numeryx.AuthorizationServiceApplication.mapper;

import com.numeryx.AuthorizationServiceApplication.dto.request.UpdateUserProfileRequest;
import com.numeryx.AuthorizationServiceApplication.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, uses = {AddressMapper.class})
public interface UserProfileResponseMapper extends EntityMapper<UpdateUserProfileRequest, User> {
}
