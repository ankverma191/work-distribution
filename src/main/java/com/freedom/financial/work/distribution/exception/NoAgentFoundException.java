package com.freedom.financial.work.distribution.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NoAgentFoundException extends RuntimeException {
    private final String message;

    public NoAgentFoundException(final String message) {
        super(message);
        this.message = message;
    }
}
