package com.maptracker.issuemap.domain.issue.dto.subissue;

public record SubIssueCreateRequestDto(
        Long issueId,
        String title
) {
}
