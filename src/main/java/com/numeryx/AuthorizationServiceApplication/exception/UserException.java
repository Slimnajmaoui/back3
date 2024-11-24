package com.numeryx.AuthorizationServiceApplication.exception;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserException extends RuntimeException {
    public enum ReasonCode implements IReasonCode {
        USERNAME_ALREADY_EXIST, IDENTIFICATION_ALREADY_EXIST, INVALID_EMAIL_USER,
        UNAUTHORIZED_ACTION, WRONG_PASSWORD, USER_NOT_FOUND, WEAK_PASSWORD, NONEXISTENT_ACCOUNT_USER, ROLE_USER_NOT_EXIST,
        ACCOUNT_NUMBER_ALREADY_EXIST,
        USER_ALREADY_EXISTS_FOR_SUBSCRIBER
    }

    private List<Reason> reasons = new ArrayList<>();

    public UserException(List<Reason> reasons) {
        this.reasons = reasons;
    }
}
