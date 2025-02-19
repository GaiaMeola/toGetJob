package org.example.togetjob.exceptions;

public class InvalidCompanyNameException extends RuntimeException {
    public InvalidCompanyNameException(String message) {
        super(message);
    }
}
