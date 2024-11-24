package com.numeryx.AuthorizationServiceApplication.service.impl;

import com.numeryx.AuthorizationServiceApplication.model.Errors;
import com.numeryx.AuthorizationServiceApplication.repository.IErrorsRepository;
import com.numeryx.AuthorizationServiceApplication.service.ErrorsService;
import org.springframework.stereotype.Service;


@Service("errorsService")
public class ErrorsServiceImpl implements ErrorsService {
    private final IErrorsRepository exceptionRepository;

    public ErrorsServiceImpl(IErrorsRepository exceptionRepository) {
        this.exceptionRepository = exceptionRepository;
    }

    @Override
    public Errors saveErrors(int status, String message, String errors, String errorCode) {
        Errors exception = new Errors();
        exception.setStatus(status);
        exception.setMessage(message);
        exception.setErrorCode(errorCode);
        exception.setErrors(errors);
        exceptionRepository.save(exception);
        return exception;
    }
}

