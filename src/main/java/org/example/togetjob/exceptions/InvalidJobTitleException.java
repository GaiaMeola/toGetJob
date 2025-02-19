package org.example.togetjob.exceptions;

public class InvalidJobTitleException extends RuntimeException {
    public InvalidJobTitleException(String message) {
        super(message);
    }
}
