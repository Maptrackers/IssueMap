package com.maptracker.issuemap.domain.issue.controller;

import com.maptracker.issuemap.domain.issue.dto.issue.*;
import com.maptracker.issuemap.domain.issue.service.IssueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    @Operation(
            summary = "이슈 생성 API",
            description = "새로운 이슈를 생성하고, 생성된 이슈 정보를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이슈 생성 성공"),
                    @ApiResponse(responseCode = "400", description = "유효하지 않은 요청 데이터")
            }
    )
    @PostMapping
    public ResponseEntity<IssueCreateRequestDto> createIssue(
            @RequestBody IssueCreateRequestDto requestDto,
            @RequestParam Long userId
    ) {
        IssueCreateRequestDto response = issueService.createIssue(requestDto, userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "이슈 상세 조회 API", description = "특정 이슈의 상세 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이슈 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "이슈를 찾을 수 없음")
            })
    @GetMapping("/{issueId}")
    public ResponseEntity<IssueResponseDto> getIssueById(@PathVariable Long issueId) {
        IssueResponseDto response = issueService.getIssueById(issueId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "프로젝트별 이슈 목록 조회 API", description = "특정 프로젝트에 속한 이슈 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이슈 목록 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음")
            })
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<IssueResponseDto>> getIssuesByProject(@PathVariable Long projectId) {
        List<IssueResponseDto> response = issueService.getIssuesByProject(projectId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "이슈 업데이트 API", description = "특정 이슈의 정보를 업데이트합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이슈 업데이트 성공"),
                    @ApiResponse(responseCode = "404", description = "이슈를 찾을 수 없음")
            })
    @PutMapping
    public ResponseEntity<IssueResponseDto> updateIssue(@RequestBody IssueUpdateRequestDto requestDto) {
        IssueResponseDto response = issueService.updateIssue(requestDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "이슈 상태 업데이트 API", description = "특정 이슈의 상태를 업데이트합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이슈 상태 업데이트 성공"),
                    @ApiResponse(responseCode = "404", description = "이슈를 찾을 수 없음")
            })
    @PatchMapping
    public ResponseEntity<IssueResponseDto> updateIssueStatus(@RequestBody IssueStatusUpdateRequestDto requestDto) {
        IssueResponseDto response = issueService.updateIssueStatus(requestDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "이슈 삭제 API", description = "특정 이슈를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이슈 삭제 성공"),
                    @ApiResponse(responseCode = "404", description = "이슈를 찾을 수 없음")
            })
    @DeleteMapping("/{issueId}")
    public ResponseEntity<IssueResponseDto> deleteIssue(@PathVariable Long issueId) {
        IssueResponseDto response = issueService.deleteIssue(issueId);
        return ResponseEntity.ok(response);
    }
}
