package org.example.togetjob.exceptions;

public class JobApplicationAlreadyProcessedException extends RuntimeException {
    public JobApplicationAlreadyProcessedException(String message) {
        super(message);
    }
}
