package com.numeryx.AuthorizationServiceApplication.mapper;

import com.numeryx.AuthorizationServiceApplication.dto.UserDto;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateUserFromRegisterRequest;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateUserRequest;
import com.numeryx.AuthorizationServiceApplication.dto.request.UpdateUserProfileRequest;
import com.numeryx.AuthorizationServiceApplication.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, uses = {AddressMapper.class})
public interface UserResponseMapper extends FactoryMapper<UserDto, User, CreateUserRequest, UpdateUserProfileRequest, CreateUserFromRegisterRequest> {
    @Mappings({
    })
    @Override
    UserDto toDto(User entity);

    @Override
    User toEntity(UserDto dto);
}
