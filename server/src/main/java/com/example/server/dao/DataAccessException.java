package com.example.server.dao;

public class DataAccessException extends Exception {
    DataAccessException(String message) {
        super(message);
    }

    DataAccessException() {
        super();
    }
}
