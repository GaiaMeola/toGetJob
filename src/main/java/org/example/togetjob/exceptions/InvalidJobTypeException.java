package org.example.togetjob.exceptions;

public class InvalidJobTypeException extends RuntimeException {
    public InvalidJobTypeException(String message) {
        super(message);
    }
}
