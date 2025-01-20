package com.maptracker.issuemap.domain.issue.controller;

import com.maptracker.issuemap.domain.issue.dto.issuehistory.IssueHistoryResponseDto;
import com.maptracker.issuemap.domain.issue.entity.IssueHistory;
import com.maptracker.issuemap.domain.issue.service.IssueHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/issuehistories")
@RequiredArgsConstructor
@Tag(name = "IssueHistory Controller", description = "이슈 기록 API")
public class IssueHistoryController {

    private final IssueHistoryService issueHistoryService;


    @Operation(summary = "이슈별 이슈 기록 조회 API", description = "특정 이슈의 기록 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이슈 기록 조회 성공")
            })
    @GetMapping("/{issueId}")
    public ResponseEntity<List<IssueHistoryResponseDto>> gitIssueHistoryByIssue(
            @PathVariable Long issueId
    ) {
        List<IssueHistoryResponseDto> response = issueHistoryService.getIssuehistoryByIssue(issueId);
        return ResponseEntity.ok(response);
    }
}
