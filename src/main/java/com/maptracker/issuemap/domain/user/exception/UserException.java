package com.maptracker.issuemap.domain.user.exception;

import com.maptracker.issuemap.common.error.ErrorCodeInterface;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

    private final ErrorCodeInterface errorCode;
    private final String errorDescription;


    public UserException(ErrorCodeInterface errorCodeInterface) {
        super(errorCodeInterface.getDescription());
        this.errorCode = errorCodeInterface;
        this.errorDescription = errorCodeInterface.getDescription();
    }

    public UserException(ErrorCodeInterface errorCodeInterface, String errorDescription) {
        super(errorDescription);
        this.errorCode = errorCodeInterface;
        this.errorDescription = errorDescription;
    }

    public UserException(ErrorCodeInterface errorCodeInterface, Throwable tx) {
        super(tx);
        this.errorCode = errorCodeInterface;
        this.errorDescription = errorCodeInterface.getDescription();
    }

    public UserException(ErrorCodeInterface errorCodeInterface, Throwable tx, String errorDescription) {
        super(tx);
        this.errorCode = errorCodeInterface;
        this.errorDescription = errorDescription;
    }
}
