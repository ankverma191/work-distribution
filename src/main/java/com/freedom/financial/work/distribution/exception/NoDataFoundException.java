package com.freedom.financial.work.distribution.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NoDataFoundException extends RuntimeException {
    private final String message;

    public NoDataFoundException(final String message) {
        super(message);
        this.message = message;
    }
}
