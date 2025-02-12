package org.example.togetjob.exceptions;

public class JobAnnouncementAlreadyExists extends RuntimeException {
    public JobAnnouncementAlreadyExists(String message) {
        super(message);
    }
}
