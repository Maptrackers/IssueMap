package com.maptracker.issuemap.domain.project.controller;

import com.maptracker.issuemap.domain.project.dto.ProjectRequestDto;
import com.maptracker.issuemap.domain.project.dto.ProjectResponseDto;
import com.maptracker.issuemap.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    // 프로젝트 생성
    @Operation(summary = "프로젝트 생성", description = "새로운 프로젝트를 생성합니다.")
    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(@RequestBody ProjectRequestDto projectRequestDto) {
        ProjectResponseDto projectResponseDto = projectService.createProject(projectRequestDto);
        return ResponseEntity.ok(projectResponseDto);
    }

    // 프로젝트 조회
    @Operation(summary = "특정 프로젝트 조회", description = "특정 프로젝트의 상세 정보를 조회합니다.")
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDto> getProjectById(@Parameter(description = "조회할 프로젝트의 ID", required = true)
                                                                 @PathVariable Long projectId) {
        ProjectResponseDto projects = projectService.getProjectById(projectId);
        return ResponseEntity.ok(projects);
    }
}