package com.maptracker.issuemap.domain.issue.dto.issue;


public record IssueCreateRequestDto(
        Long projectId,
        String title,
        String issueType
) {
}
