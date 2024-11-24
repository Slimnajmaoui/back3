package com.numeryx.AuthorizationServiceApplication.service;


import com.numeryx.AuthorizationServiceApplication.model.Errors;


public interface ErrorsService {

    Errors saveErrors(int status, String message, String errors, String errorCode);
}
