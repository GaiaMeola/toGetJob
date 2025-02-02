package org.example.togetjob.exceptions;

public class JobAnnouncementNotFoundException extends RuntimeException {

  public JobAnnouncementNotFoundException(String message) {
        super(message);
    }

    public JobAnnouncementNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
