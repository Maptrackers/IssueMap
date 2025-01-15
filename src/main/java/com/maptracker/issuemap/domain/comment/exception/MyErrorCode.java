package com.maptracker.issuemap.domain.comment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MyErrorCode {
    ISSUE_NOT_FOUND("해당 이슈가 존재하지 않습니다."),
    USER_NOT_FOUND("해당 유저가 존재하지 않습니다."),
    COMMENT_NOT_FOUND("해당 부모 댓글이 존재하지 않습니다.");

    private final String message;
}