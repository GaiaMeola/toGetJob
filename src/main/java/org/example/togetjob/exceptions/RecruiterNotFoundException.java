package org.example.togetjob.exceptions;

public class RecruiterNotFoundException extends RuntimeException {
    public RecruiterNotFoundException(String message) {
        super(message);
    }
}
