package com.example.englishpolish.exception;

public class WordAlreadyExistException extends Exception{
    public WordAlreadyExistException (String errorMessage) {
        super(errorMessage);
    }
}
