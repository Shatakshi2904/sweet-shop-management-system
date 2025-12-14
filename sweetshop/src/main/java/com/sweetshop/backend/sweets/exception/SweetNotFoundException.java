package com.sweetshop.backend.sweets.exception;

public class SweetNotFoundException extends RuntimeException {

    public SweetNotFoundException(String message) {
        super(message);
    }
}
