package com.numeryx.AuthorizationServiceApplication.exception;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidationException extends RuntimeException {

    public enum ReasonCode implements IReasonCode {
        IS_NULL, IS_EMPTY, INVALID, INVALID_EMAIL_USER, IDENTIFICATION_ALREADY_EXIST, USERNAME_ALREADY_EXIST,
        ID_ALREADY_EXIST,WRONG_FUNCTION, INVALID_EMAIL, ALREADY_EXISTS, DOES_NOT_EXISTS
    }

    private List<Reason> reasons = new ArrayList<>();

    public ValidationException(List<Reason> reasons) {
        this.reasons = reasons;
    }

    public ValidationException(String msg) {
        super(msg);
    }
}
