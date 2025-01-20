package com.maptracker.issuemap.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "댓글 정보 요청 dto")
public class IssueCommentCreateDto {

    @Schema(description = "댓글 내용",example = "댓글 내용")
    private String content; // 댓글 내용
//    private Long issueId; // 연관된 이슈 ID
    @Schema(description = "부모 댓글 ID. 최상위 댓글의 경우 null", example = "null 또는 1")
    private Long parentCommentId; // 부모 댓글 ID (없으면 null)

    @Builder
    public IssueCommentCreateDto( Long parentCommentId, String content) {
        this.parentCommentId = parentCommentId;
        this.content = content;
    }
}