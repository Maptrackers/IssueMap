package com.maptracker.issuemap.domain.issue.dto.subissue;

public record SubIssueStatusUpdateRequestDto(
        Long subIssueId,
        String status
) {
}
