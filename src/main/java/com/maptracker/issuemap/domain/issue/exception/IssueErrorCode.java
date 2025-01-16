package com.maptracker.issuemap.domain.issue.exception;

import com.maptracker.issuemap.common.error.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IssueErrorCode implements ErrorCodeInterface {

    // 아직 사용 안함
    INVALID_ISSUE_TYPE(400, 1001, "유효한 타입이 아닙니다."), // 새 에러 코드 추가
    ISSUE_NOT_FOUND(404, 1004, "해당 이슈가 존재하지 않습니다.");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
