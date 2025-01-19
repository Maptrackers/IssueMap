package com.maptracker.issuemap.domain.issue.dto.subissue;

public record SubIssueUpdateRequestDto(
        Long subIssueId,
        String title,
        String content
) {
}
