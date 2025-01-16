package com.maptracker.issuemap.domain.comment.exception;

public class MyException extends RuntimeException {
    private final MyErrorCode errorCode;

    public MyException(MyErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public MyErrorCode getErrorCode() {
        return errorCode;
    }
}