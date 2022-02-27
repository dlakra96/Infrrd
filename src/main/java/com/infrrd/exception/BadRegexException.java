package com.infrrd.exception;

public class BadRegexException extends RuntimeException{
    public BadRegexException(String message){
        super(message);
    }
}
