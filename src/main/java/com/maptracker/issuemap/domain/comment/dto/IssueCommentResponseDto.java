package com.maptracker.issuemap.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "댓글 정보 응답 dto")
public class IssueCommentResponseDto {

    @Schema(description = "댓글 id값",example = "1")
    private Long id;

    @Schema(description = "댓글 내용",example = "댓글 내용")
    private String content;

    @Schema(description = "이슈 id 값",example = "1")
    private Long issueId;

    @Schema(description = "첫 댓글인 경우 null, 대댓글인 경우 댓글 id 값 ",example = "null or 1")
    private Long parentCommentId;

    @Schema(description = "유저 id 값",example = "1")
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