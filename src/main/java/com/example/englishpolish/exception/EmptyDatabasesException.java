package com.example.englishpolish.exception;

public class EmptyDatabasesException extends Exception{
    public EmptyDatabasesException(String errorMessage) {
        super(errorMessage);
    }
}
