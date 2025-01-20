package com.maptracker.issuemap.domain.issue.controller;

import com.maptracker.issuemap.domain.issue.dto.subissue.*;
import com.maptracker.issuemap.domain.issue.service.SubIssueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subissues")
@RequiredArgsConstructor
@Tag(name = "SubIssue Controller", description = "하위 이슈 API")
public class SubIssueController {

    private final SubIssueService subIssueService;

    @Operation(
            summary = "하위 이슈 생성",
            description = "새로운 하위 이슈를 생성하고 생성된 하위 이슈의 정보를 반환",
            responses = {
                    @ApiResponse(responseCode = "200", description = "하위 이슈 생성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
            }
    )
    @PostMapping
    public ResponseEntity<SubIssueCreateRequestDto> createSubIssue(
            @RequestBody SubIssueCreateRequestDto subIssueCreateRequestDto,
            @RequestParam Long userId) {
        SubIssueCreateRequestDto response = subIssueService.create(subIssueCreateRequestDto, userId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "하위 이슈 상세 조회",
            description = "하위 이슈 ID로 특정 하위 이슈의 상세 정보를 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "하위 이슈 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "하위 이슈를 찾을 수 없음")
            }
    )
    @GetMapping("/{subIssueId}")
    public ResponseEntity<SubIssueResponseDto> getSubIssueById(
            @PathVariable Long subIssueId
    ) {
        SubIssueResponseDto response = subIssueService.getSubIssueById(subIssueId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "이슈별 하위 이슈 목록 조회",
            description = "특정 이슈의 모든 하위 이슈를 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "하위 이슈 목록 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "이슈를 찾을 수 없음")
            }
    )
    @GetMapping("/issue/{issueId}")
    public ResponseEntity<List<SubIssueResponseDto>> getSubIssuesByIssue(
            @PathVariable Long issueId
    ) {
        List<SubIssueResponseDto> response = subIssueService.getSubIssueByIssue(issueId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "하위 이슈 수정",
            description = "특정 하위 이슈의 제목 또는 내용을 수정",
            responses = {
                    @ApiResponse(responseCode = "200", description = "하위 이슈 수정 성공"),
                    @ApiResponse(responseCode = "404", description = "하위 이슈를 찾을 수 없음")
            }
    )
    @PutMapping
    public ResponseEntity<SubIssueResponseDto> updateSubIssueTitle(
            @RequestBody SubIssueUpdateRequestDto subIssueUpdateRequestDto
    ) {
        SubIssueResponseDto response = subIssueService.updateSubIssue(subIssueUpdateRequestDto);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "하위 이슈 상태 변경",
            description = "특정 하위 이슈의 상태 변경",
            responses = {
                    @ApiResponse(responseCode = "200", description = "하위 이슈 상태 변경 성공"),
                    @ApiResponse(responseCode = "404", description = "하위 이슈를 찾을 수 없음"),
                    @ApiResponse(responseCode = "400", description = "잘못된 상태 값")
            }
    )
    @PatchMapping("/status/{userId}")
    public ResponseEntity<SubIssueResponseDto> patchSubIssueStatus(
            @RequestBody SubIssueUpdateStatusRequestDto subIssueUpdateStatusRequestDto,
            @PathVariable Long userId
    ) {
        SubIssueResponseDto response = subIssueService.updateSubIssueStatus(subIssueUpdateStatusRequestDto);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "하위 이슈 삭제",
            description = "특정 하위 이슈를 ID로 삭제",
            responses = {
                    @ApiResponse(responseCode = "200", description = "하위 이슈 삭제 성공"),
                    @ApiResponse(responseCode = "404", description = "하위 이슈를 찾을 수 없음")
            }
    )
    @DeleteMapping("/{subIssueId}")
    public ResponseEntity<SubIssueResponseDto> deleteSubIssue(
            @PathVariable Long subIssueId
    ) {
        SubIssueResponseDto response = subIssueService.deleteSubIssue(subIssueId);
        return ResponseEntity.ok(response);
    }
}