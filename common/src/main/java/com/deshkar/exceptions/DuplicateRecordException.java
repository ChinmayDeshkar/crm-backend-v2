package com.deshkar.exceptions;

public class DuplicateRecordException extends LoginException{
    public DuplicateRecordException(String msg){
        super(msg);
    }
}
