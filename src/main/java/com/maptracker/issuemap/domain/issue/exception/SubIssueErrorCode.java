package com.maptracker.issuemap.domain.issue.exception;


import com.maptracker.issuemap.common.error.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SubIssueErrorCode implements ErrorCodeInterface {
    // 아직 사용 안함
    SUBISSUE_NOT_FOUND(404, 1004, "해당 하위 이슈가 존재하지 않습니다.");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
