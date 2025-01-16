package com.maptracker.issuemap.domain.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponseDto {
    @Schema(description = "프로젝트 ID", example = "1")
    private Long id;
    @Schema(description = "프로젝트 이름", example = "IssueMap")
    private String projectName;
    @Schema(description = "생성일", example = "2025.01.01")
    private LocalDateTime createdAt;
    @Schema(description = "팀 ID", example = "1")
    private Long teamId;
    @Schema(description = "팀 이름", example = "MapTrackers")
    private String teamName;
}
