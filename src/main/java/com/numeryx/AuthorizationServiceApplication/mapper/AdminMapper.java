package com.numeryx.AuthorizationServiceApplication.mapper;

import com.numeryx.AuthorizationServiceApplication.dto.request.CreateAdminDTO;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateUserRequest;
import com.numeryx.AuthorizationServiceApplication.model.Admin;
import com.numeryx.AuthorizationServiceApplication.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, uses = {AddressMapper.class})
public interface AdminMapper extends EntityMapper<CreateAdminDTO, Admin> {
}
