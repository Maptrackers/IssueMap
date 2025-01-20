package com.maptracker.issuemap.domain.issue.dto.issue;


public record IssueUpdateContentRequestDto(
        Long issueId,
        String content
) {
}
