package com.numeryx.AuthorizationServiceApplication.service;

import com.numeryx.AuthorizationServiceApplication.dto.MinifiedUserDTO;
import com.numeryx.AuthorizationServiceApplication.dto.UserDto;
import com.numeryx.AuthorizationServiceApplication.dto.request.ChangeUserConfidentiality;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateUserRequest;
import com.numeryx.AuthorizationServiceApplication.dto.request.UpdateUserProfileRequest;

import java.util.ArrayList;
import java.util.List;

public interface IUserService extends IUserManagement<CreateUserRequest, UserDto>, IUserAccountManagement {

    UserDto getMe();
    List<MinifiedUserDTO> getUserByUserName(List<String> userNames);
    UserDto updateProfile(UpdateUserProfileRequest updateUserProfileRequest);
    UserDto changeUserPassword(ChangeUserConfidentiality changeUserConfidentiality);
    List<MinifiedUserDTO> getUserByIds(List<Long> ids);
    List<MinifiedUserDTO> getAllProjectDirector();
    List<MinifiedUserDTO> getAllFinancier();
    Long getSubscriberId(Long id);
}
