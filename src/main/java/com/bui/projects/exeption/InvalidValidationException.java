package com.bui.projects.exeption;

public class InvalidValidationException extends RuntimeException {

    public InvalidValidationException(String message) {
        super(message);
    }
}
