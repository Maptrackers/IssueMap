package com.maptracker.issuemap.domain.issue.dto.issue;


public record IssueUpdateTitleRequestDto(
        Long issueId,
        String title
) {
}
