package com.maptracker.issuemap.domain.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUpdateRequestDto {
    @Schema(description = "팀 이름", example = "MapTrackers")
    private String projectName;
}