package org.example.togetjob.exceptions;

public class InvalidUsernameException extends RuntimeException {
    public InvalidUsernameException(String message)
    {
        super(message);
    }
}
