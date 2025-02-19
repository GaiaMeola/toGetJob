package org.example.togetjob.exceptions;

public class InvalidJobDescriptionException extends RuntimeException {
    public InvalidJobDescriptionException(String message) {
        super(message);
    }
}
