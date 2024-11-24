package com.numeryx.AuthorizationServiceApplication.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Reason<T extends IReasonCode> {
    private T code;
    private String message;

    public Reason(T code) {
        this.code = code;
    }
}
