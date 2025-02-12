package org.example.togetjob.exceptions;

public class InvalidSalaryException extends RuntimeException {
    public InvalidSalaryException(String message) {
        super(message);
    }
}
