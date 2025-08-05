package com.pegasus.goplaneje.exceptions;

public class CredentialsInUseException extends BusinessException {
    public CredentialsInUseException(String credential) {
        super("Credential already in use: " + credential);
    }
}
