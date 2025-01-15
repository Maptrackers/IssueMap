package com.maptracker.issuemap.domain.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IssueCommentRequestDto {
    private String content; // 댓글 내용
    private Long issueId; // 연관된 이슈 ID
    private Long parentCommentId; // 부모 댓글 ID (없으면 null)

    @Builder
    public IssueCommentRequestDto(Long issueId, Long parentCommentId, String content) {
        this.issueId = issueId;
        this.parentCommentId = parentCommentId;
        this.content = content;
    }
}