package com.maptracker.issuemap.domain.issue.dto.subissue;

import com.maptracker.issuemap.domain.issue.entity.SubIssue;

import java.time.LocalDateTime;

public record SubIssueResponseDto(
        Long id,
        String title,
        String content,
        String issueStatus,
        Long issueId,
        String issueTitle,
        Long userId,
        String userName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static SubIssueResponseDto fromEntity(SubIssue subIssue) {
        return new SubIssueResponseDto(
                subIssue.getId(),
                subIssue.getTitle(),
                subIssue.getContent(),
                subIssue.getIssueStatus().name(),
                subIssue.getIssue().getId(),
                subIssue.getIssue().getTitle(),
                subIssue.getUser().getId(),
                subIssue.getUser().getUsername(),
                subIssue.getCreatedAt(),
                subIssue.getUpdatedAt()
        );
    }
}
