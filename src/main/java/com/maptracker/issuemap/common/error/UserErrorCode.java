package com.maptracker.issuemap.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCodeInterface{
    USER_ALREADY_EXIST(400, 1000, "이미 존재하는 이메일 입니다."),
    USER_NOT_FOUND(404, 1001, "해당 사용자의 정보가 존재하지 않습니다."),
    USER_NOT_AUTHENTICATED(403, 1002, "해당 기능은 로그인이 필요합니다.");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
