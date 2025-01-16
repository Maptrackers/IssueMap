package com.maptracker.issuemap.domain.issue.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST) // 기본적으로 BAD_REQUEST로 설정
public class IssueCustomException extends RuntimeException {

    private final Integer httpStatusCode; // HTTP 상태 코드
    private final Integer errorCode;      // 에러 코드
    private final String description;     // 에러 설명

    public IssueCustomException(IssueErrorCode errorCode) {
        super(errorCode.getDescription());
        this.httpStatusCode = errorCode.getHttpStatusCode();
        this.errorCode = errorCode.getErrorCode();
        this.description = errorCode.getDescription();
    }
}