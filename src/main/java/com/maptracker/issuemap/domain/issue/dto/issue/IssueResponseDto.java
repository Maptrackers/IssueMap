package com.maptracker.issuemap.domain.issue.dto.issue;

import com.maptracker.issuemap.domain.issue.entity.Issue;

import java.time.LocalDateTime;

public record IssueResponseDto(
        Long id,
        String title,
        String content,
        String issueType,
        String issueStatus,
        Long projectId,
        String projectName,
        Long userId,
        String userName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static IssueResponseDto fromEntity(Issue issue) {
        return new IssueResponseDto(
                issue.getId(),
                issue.getTitle(),
                issue.getContent(),
                issue.getIssueType().name(),
                issue.getIssueStatus().name(),
                issue.getProject().getProjectId(),
                issue.getProject().getProjectName(),
                issue.getUser().getId(),
                issue.getUser().getUsername(),
                issue.getCreatedAt(),
                issue.getUpdatedAt()
        );
    }
}