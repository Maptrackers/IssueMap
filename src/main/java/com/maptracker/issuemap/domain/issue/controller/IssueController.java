package com.maptracker.issuemap.domain.issue.controller;

import com.maptracker.issuemap.domain.issue.dto.issue.IssueCreateRequestDto;
import com.maptracker.issuemap.domain.issue.service.IssueService;
import com.maptracker.issuemap.domain.team.dto.TeamResponseDto;
import com.maptracker.issuemap.domain.team.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/issue")
public class IssueController {

    private final IssueService issueService;
    private final TeamService teamService;

    @Operation(
            summary = "이슈 생성 API",
            description = "새로운 이슈를 생성하고, 생성된 이슈 정보를 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "이슈 생성 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IssueCreateRequestDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "유효하지 않은 요청 데이터",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping
    public ResponseEntity<IssueCreateRequestDto> createIssue(
            @RequestBody IssueCreateRequestDto issueCreateRequestDto,
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(issueService.createIssue(issueCreateRequestDto, userId));
    }

}

