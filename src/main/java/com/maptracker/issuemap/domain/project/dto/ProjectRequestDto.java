package com.maptracker.issuemap.domain.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequestDto {
    @Schema(description = "팀 이름", example = "MapTrackers")
    private String projectName;
    @Schema(description = "팀 ID", example = "1")
    private Long teamId;
}
