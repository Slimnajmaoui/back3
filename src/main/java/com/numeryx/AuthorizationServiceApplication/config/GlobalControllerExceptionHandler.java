package com.numeryx.AuthorizationServiceApplication.config;

import com.numeryx.AuthorizationServiceApplication.dto.error.ErrorWithReasons;
import com.numeryx.AuthorizationServiceApplication.exception.*;
import com.numeryx.AuthorizationServiceApplication.service.impl.ErrorsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    private final ErrorsServiceImpl errorsService;

    public GlobalControllerExceptionHandler(ErrorsServiceImpl errorsService) {
        this.errorsService = errorsService;
    }

    @ExceptionHandler({UserException.class})
    public ResponseEntity handleUser(UserException ex) {
        errorsService.saveErrors(HttpStatus.BAD_REQUEST.value(), "User exception", String.valueOf(ex.getReasons()),
                HttpStatus.BAD_REQUEST.name());
        return new ResponseEntity(new ErrorWithReasons(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getReasons(),
                UserException.class.getSimpleName()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity handleValidation(ValidationException ex) {
        errorsService.saveErrors(HttpStatus.BAD_REQUEST.value(), "Object validation failed",
                String.valueOf(ex.getReasons()), ValidationException.class.getSimpleName());
        return new ResponseEntity(new ErrorWithReasons(HttpStatus.BAD_REQUEST, "Object validation failed",
                ex.getReasons(), ValidationException.class.getSimpleName()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity handleUsernameNotFoundException(UsernameNotFoundException ex) {
        errorsService.saveErrors(HttpStatus.BAD_REQUEST.value(), "Username not found", String.valueOf(ex.getReasons()),
                UsernameNotFoundException.class.getSimpleName());
        return new ResponseEntity(new ErrorWithReasons(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getReasons(),
                UsernameNotFoundException.class.getSimpleName()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity handleEntityNotFoundException(EntityNotFoundException ex) {
        errorsService.saveErrors(HttpStatus.BAD_REQUEST.value(), "Entity not found", String.valueOf(ex.getReasons()),
                EntityNotFoundException.class.getSimpleName());
        return new ResponseEntity(new ErrorWithReasons(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getReasons(),
                EntityNotFoundException.class.getSimpleName()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({TokenException.class})
    public ResponseEntity handleToken(TokenException ex) {
        errorsService.saveErrors(HttpStatus.BAD_REQUEST.value(), "Token exception", String.valueOf(ex.getReasons()),
                TokenException.class.getSimpleName());
        return new ResponseEntity(new ErrorWithReasons(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getReasons(),
                TokenException.class.getSimpleName()), HttpStatus.BAD_REQUEST);
    }
}
