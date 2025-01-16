package com.maptracker.issuemap.domain.comment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MyErrorCode {
    ISSUE_NOT_FOUND("해당 이슈가 존재하지 않습니다."),
    USER_NOT_FOUND("해당 유저가 존재하지 않습니다."),
    ISSUE_MISMATCH( "댓글이 이슈와 일치하지 않습니다."),
    UNAUTHORIZED_USER("댓글 작성자가 아닙니다."),
    COMMENT_NOT_FOUND( "해당 댓글을 찾을 수 없습니다.");

    private final String message;
}