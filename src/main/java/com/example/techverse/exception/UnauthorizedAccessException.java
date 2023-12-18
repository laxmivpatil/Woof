package com.example.techverse.exception;

public class UnauthorizedAccessException extends Exception {

    public UnauthorizedAccessException() {
        super("Unauthorized access");
    }

    public UnauthorizedAccessException(String message) {
        super(message);
    }

    public UnauthorizedAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}

