package com.maptracker.issuemap.domain.issue.dto.issue;

public record IssueUpdateStatusRequestDto(
        Long issueId,
        String status
) {
}
