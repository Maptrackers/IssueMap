package com.maptracker.issuemap.domain.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IssueCommentResponseDto {
    private Long id;
    private String content;
    private Long issueId;
    private Long parentCommentId;
    private Long userId;


    @Builder
    public IssueCommentResponseDto(Long id, String content, Long issueId, Long parentCommentId, Long userId) {
        this.id = id;
        this.content = content;
        this.issueId = issueId;
        this.parentCommentId = parentCommentId;
        this.userId = userId;
    }
}