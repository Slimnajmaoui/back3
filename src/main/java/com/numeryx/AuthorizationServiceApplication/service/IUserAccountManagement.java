package com.numeryx.AuthorizationServiceApplication.service;

import com.numeryx.AuthorizationServiceApplication.dto.request.ResetTokenRequest;
import com.numeryx.AuthorizationServiceApplication.model.User;

public interface IUserAccountManagement {

    default ResetTokenRequest setMailRequest(User entity, String token, Boolean changeMail) {
        ResetTokenRequest resetPasswordRequest = new ResetTokenRequest();
        if(changeMail) {
            resetPasswordRequest.setEmail(entity.getUsernameChange());
        } else {
            resetPasswordRequest.setEmail(entity.getUsername());
        }
        resetPasswordRequest.setToken(token);
        resetPasswordRequest.setUserFirstName(entity.getFirstname() + " " + entity.getLastname());
        resetPasswordRequest.setId(entity.getId());
        return resetPasswordRequest;
    }
}
