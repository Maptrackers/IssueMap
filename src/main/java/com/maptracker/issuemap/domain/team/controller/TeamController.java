package com.maptracker.issuemap.domain.team.controller;

import com.maptracker.issuemap.domain.team.dto.TeamRequestDto;
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
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @Operation(
            summary = "팀 생성 API",
            description = "새로운 팀을 생성합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "팀 생성 성공",
                            content = @Content(schema = @Schema(implementation = TeamResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
            }
    )
    @PostMapping
    public ResponseEntity<TeamResponseDto> createTeam(@RequestBody TeamRequestDto teamRequestDto) {
        TeamResponseDto teamResponseDto = teamService.createTeam(teamRequestDto);
        return ResponseEntity.ok(teamResponseDto);
    }

    @Operation(
            summary = "팀 조회 API",
            description = "팀 ID를 기반으로 팀 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "팀 조회 성공",
                            content = @Content(schema = @Schema(implementation = TeamResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없음")
            }
    )
    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponseDto> getTeam(@PathVariable Long teamId) {
        TeamResponseDto teamResponseDto = teamService.getTeam(teamId);
        return ResponseEntity.ok(teamResponseDto);
    }

    @Operation(
            summary = "팀 수정 API",
            description = "팀 ID를 기반으로 팀 정보를 수정합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "팀 수정 성공",
                            content = @Content(schema = @Schema(implementation = TeamResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청 데이터"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "팀을 찾을 수 없음"
                    )
            }
    )
    @PutMapping("/{teamId}")
    public ResponseEntity<TeamResponseDto> updateTeam(@PathVariable Long teamId, @RequestBody TeamRequestDto teamRequestDto){
        TeamResponseDto teamResponseDto = teamService.updateTeam(teamId, teamRequestDto);
        return ResponseEntity.ok(teamResponseDto);
    }

    @Operation(
            summary = "팀 삭제 API",
            description = "팀 ID를 기반으로 팀을 삭제합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "팀 삭제 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "팀을 찾을 수 없음"
                    )
            }
    )
    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long teamId) {
        teamService.deleteTeam(teamId);
        return ResponseEntity.noContent().build();
    }
}
