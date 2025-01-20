package com.maptracker.issuemap.domain.issue.dto.subissue;

public record SubIssueUpdateStatusRequestDto(
        Long subIssueId,
        String status
) {
}
