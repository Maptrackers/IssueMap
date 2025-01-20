package com.maptracker.issuemap.domain.issue.controller;

import com.maptracker.issuemap.domain.issue.dto.issue.*;
import com.maptracker.issuemap.domain.issue.service.IssueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
@Tag(name = "Issue Controller", description = "이슈 API")
public class IssueController {

    private final IssueService issueService;

    @Operation(
            summary = "이슈 생성 API",
            description = "새로운 이슈를 생성하고, 생성된 이슈 정보를 반환",
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

    @Operation(summary = "이슈 상세 조회 API", description = "특정 이슈의 상세 정보를 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이슈 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "이슈를 찾을 수 없음")
            })
    @GetMapping("/{issueId}")
    public ResponseEntity<IssueResponseDto> getIssueById(@PathVariable Long issueId) {
        IssueResponseDto response = issueService.getIssueById(issueId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "프로젝트별 이슈 목록 조회 API", description = "특정 프로젝트의 이슈 목록 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이슈 목록 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음")
            })
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<IssueResponseDto>> getIssuesByProject(@PathVariable Long projectId) {
        List<IssueResponseDto> response = issueService.getIssuesByProject(projectId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "이슈 제목 업데이트 API", description = "특정 이슈 제목 업데이트",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이슈 제목 업데이트 성공"),
                    @ApiResponse(responseCode = "404", description = "이슈를 찾을 수 없음")
            })
    @PatchMapping("/title/{userId}")
    public ResponseEntity<IssueResponseDto> updateIssueTitle(
            @RequestBody IssueUpdateTitleRequestDto requestDto,
            @PathVariable Long userId
    ) {
        IssueResponseDto response = issueService.updateIssueTitle(requestDto, userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "이슈 내용 업데이트 API", description = "특정 이슈 내용 업데이트",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이슈 내용 업데이트 성공"),
                    @ApiResponse(responseCode = "404", description = "이슈를 찾을 수 없음")
            })
    @PatchMapping("/content/{userId}")
    public ResponseEntity<IssueResponseDto> updateIssueContent(
            @RequestBody IssueUpdateContentRequestDto requestDto,
            @PathVariable Long userId
    ) {
        IssueResponseDto response = issueService.updateIssueContent(requestDto, userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "이슈 상태 업데이트 API", description = "특정 이슈의 상태 업데이트",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이슈 상태 업데이트 성공"),
                    @ApiResponse(responseCode = "404", description = "이슈를 찾을 수 없음")
            })
    @PatchMapping("/status/{userId}")
    public ResponseEntity<IssueResponseDto> updateIssueStatus(
            @RequestBody IssueUpdateStatusRequestDto requestDto,
            @PathVariable Long userId
    ) {
        IssueResponseDto response = issueService.updateIssueStatus(requestDto, userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "이슈 삭제 API", description = "특정 이슈 삭제",
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
