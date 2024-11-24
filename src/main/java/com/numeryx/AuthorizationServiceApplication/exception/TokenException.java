package com.numeryx.AuthorizationServiceApplication.exception;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TokenException extends RuntimeException {
    public enum ReasonCode implements IReasonCode {
        WRONG_TOKEN, TOKEN_EXPIRED, VALIDATION_TOKEN_EXPIRED, VALIDATION_CODE_EXPIRED,
        WRONG_VALIDATION_CODE, WRONG_VALIDATION_CODE_EXCEPTION, FAILED_CREATED_COMPTE_EXCEPTION
    }

    private List<Reason> reasons = new ArrayList<>();

    public TokenException(List<Reason> reasons) {
        this.reasons = reasons;
    }
}
