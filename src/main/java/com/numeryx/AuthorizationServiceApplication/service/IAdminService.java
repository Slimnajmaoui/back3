package com.numeryx.AuthorizationServiceApplication.service;

import com.numeryx.AuthorizationServiceApplication.dto.AdminDTO;
import com.numeryx.AuthorizationServiceApplication.dto.UserDto;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateAdminDTO;

public interface IAdminService extends IUserManagement<CreateAdminDTO, AdminDTO>, IUserAccountManagement {

    UserDto enableDisableUser(Long id);

    UserDto setAccountBlocked(Long id);
}
