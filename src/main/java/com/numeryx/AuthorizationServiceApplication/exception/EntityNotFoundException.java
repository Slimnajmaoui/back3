package com.numeryx.AuthorizationServiceApplication.exception;

import lombok.Data;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

@Data
public class EntityNotFoundException extends PersistenceException {
    public enum ReasonCode implements IReasonCode {
        INVALID_EMAIL, USER_NOT_FOUND, SUBSCRIBER_NOT_FOUND, ENTITY_NOT_FOUND
    }

    private List<Reason> reasons = new ArrayList<>();

    public EntityNotFoundException(List<Reason> reasons) {
        this.reasons = reasons;
    }
}
