package com.numeryx.AuthorizationServiceApplication.service;

import com.numeryx.AuthorizationServiceApplication.dto.UserDto;
import com.numeryx.AuthorizationServiceApplication.dto.request.ChangeUserConfidentiality;
import com.numeryx.AuthorizationServiceApplication.dto.request.ChangeUserCredentials;
import com.numeryx.AuthorizationServiceApplication.enumeration.RoleEnum;

import java.util.List;

public interface IOpenApiService {

    void updateUserCredentials(ChangeUserCredentials changeUserCredentials);
    void resetPassword(String username);
    Boolean checkActivationCode(Long id, String token, String activationCode, boolean isResetRequest);
    Boolean checkToken(Long id, String token, boolean hasCode, boolean isResetRequest);
    UserDto sendActivationCode(Long id, String token);
    UserDto resendActivationCode(Long id, String token);
    RoleEnum getRoleByIdAndToken(Long id, String token);
    UserDto validateUserEmail(ChangeUserConfidentiality changeUserConfidentiality);


}
