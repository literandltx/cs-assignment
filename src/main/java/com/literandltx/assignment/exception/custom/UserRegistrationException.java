package com.literandltx.assignment.exception.custom;

public class UserRegistrationException extends RuntimeException {
    public UserRegistrationException(final String message) {
        super(message);
    }

    public UserRegistrationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
