package com.maptracker.issuemap.domain.issue.dto.issue;


public record IssueUpdateRequestDto(
        Long issueId,
        String title,
        String content,
        String issueType
) {
}
