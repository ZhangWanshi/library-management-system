package com.wanshi.library.exception;

public class BookNotAvailableException extends RuntimeException {

    public BookNotAvailableException(String message) {
        super(message);
    }
}