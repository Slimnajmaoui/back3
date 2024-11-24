package com.numeryx.AuthorizationServiceApplication.validator;

import com.numeryx.AuthorizationServiceApplication.dto.UserDto;
import com.numeryx.AuthorizationServiceApplication.dto.request.CreateUserRequest;
import com.numeryx.AuthorizationServiceApplication.dto.request.UpdateUserProfileRequest;

public interface UserValidator extends IGenericValidator<CreateUserRequest, UserDto> {
    void beforeUpdateProfile(UpdateUserProfileRequest updateProfileRequest);
}
