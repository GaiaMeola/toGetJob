package org.example.togetjob.exceptions;

public class UserNotLoggedException extends RuntimeException {
  public UserNotLoggedException() {
    super("User is not logged in.");
  }
}
