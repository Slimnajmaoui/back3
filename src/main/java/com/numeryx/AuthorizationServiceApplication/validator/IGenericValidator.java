package com.numeryx.AuthorizationServiceApplication.validator;

public interface IGenericValidator<C, U> {
    void beforeUpdate(U updateRequest);

    void beforeSave(C createRequest);
}
