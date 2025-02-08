package org.example.togetjob.exceptions;

import java.sql.SQLException;

public class DatabaseConnectionException extends RuntimeException {
    public DatabaseConnectionException(String message, SQLException e) {
        super(message);
    }
}
