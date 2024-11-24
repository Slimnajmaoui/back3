package com.numeryx.AuthorizationServiceApplication.dto.error;

import com.numeryx.AuthorizationServiceApplication.exception.IReasonCode;
import com.numeryx.AuthorizationServiceApplication.exception.Reason;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
public class ErrorWithReasons<T extends IReasonCode> extends ApiError {

    private List<Reason<T>> reasons = new ArrayList<>();

    public ErrorWithReasons(HttpStatus status, String message, List<Reason<T>> reasons) {
        super(status, message);
        this.reasons = reasons;
    }

    public ErrorWithReasons(HttpStatus status, String message, List<Reason<T>> reasons, String errorCode) {
        super(status, message, errorCode);
        this.reasons = reasons;
    }
}
