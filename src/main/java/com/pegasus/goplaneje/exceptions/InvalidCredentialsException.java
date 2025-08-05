package com.pegasus.goplaneje.exceptions;

public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException() {
        super("Invalid credentials");
    }
}