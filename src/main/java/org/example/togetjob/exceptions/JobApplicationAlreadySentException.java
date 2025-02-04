package org.example.togetjob.exceptions;

public class JobApplicationAlreadySentException extends RuntimeException {

    public JobApplicationAlreadySentException(String message) {
        super(message);
    }

}
