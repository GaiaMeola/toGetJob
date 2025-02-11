package org.example.togetjob.exceptions;

public class UsernameTakeException extends RuntimeException {
    public UsernameTakeException(String message) {
        super(message);
    }
}
