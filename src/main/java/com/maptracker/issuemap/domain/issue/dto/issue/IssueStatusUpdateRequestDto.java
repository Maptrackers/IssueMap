package com.maptracker.issuemap.domain.issue.dto.issue;

public record IssueStatusUpdateRequestDto(
        Long issueId,
        String status
) {
}
