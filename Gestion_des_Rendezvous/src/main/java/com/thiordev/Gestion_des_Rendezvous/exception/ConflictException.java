package com.thiordev.Gestion_des_Rendezvous.exception;

public class ConflictException extends  RuntimeException{

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
