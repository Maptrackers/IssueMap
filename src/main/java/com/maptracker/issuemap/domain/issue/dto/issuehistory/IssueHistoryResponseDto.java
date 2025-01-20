package com.maptracker.issuemap.domain.issue.dto.issuehistory;

import com.maptracker.issuemap.domain.issue.entity.IssueHistory;

import java.time.LocalDateTime;

public record IssueHistoryResponseDto(
        String userName,
        String issueHistoryType,
        String detail,
        LocalDateTime createdAt
) {
    public static IssueHistoryResponseDto fromEntity(IssueHistory issueHistory) {
        return new IssueHistoryResponseDto(
                issueHistory.getUser().getUsername(),
                issueHistory.getIssueHistoryType(),
                issueHistory.getDetail(),
                issueHistory.getCreatedAt()
        );
    }
}
