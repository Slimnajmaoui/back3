package com.numeryx.AuthorizationServiceApplication.exception;

import org.springframework.security.core.AuthenticationException;

import java.util.ArrayList;
import java.util.List;

public class UsernameNotFoundException extends AuthenticationException {

    public enum ReasonCode implements IReasonCode {
        USER_NOT_FOUND
    }

    private List<Reason> reasons = new ArrayList<>();

    public UsernameNotFoundException(String msg, List<Reason> reasons) {
        super(msg);
        this.reasons = reasons;
    }

    public List<Reason> getReasons() {
        return reasons;
    }

    public void setReasons(List<Reason> reasons) {
        this.reasons = reasons;
    }
}
