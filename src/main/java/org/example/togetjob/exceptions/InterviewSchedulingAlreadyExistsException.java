package org.example.togetjob.exceptions;

public class InterviewSchedulingAlreadyExistsException extends RuntimeException {
    public InterviewSchedulingAlreadyExistsException(String message) {
        super(message);
    }
}
