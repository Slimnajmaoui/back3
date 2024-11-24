package com.numeryx.AuthorizationServiceApplication.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiError {
    private HttpStatus status;
    private String message;
    private String errorCode;
    private List<String> errors;

    public ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiError(HttpStatus status, String message, String errorCode) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }
}
