package org.example.togetjob.exceptions;

public class InvalidWorkingHourException extends RuntimeException {
    public InvalidWorkingHourException(String message) {
        super(message);
    }
}
