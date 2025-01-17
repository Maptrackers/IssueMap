package com.maptracker.issuemap.domain.project.service;

import com.maptracker.issuemap.domain.project.dto.ProjectRequestDto;
import com.maptracker.issuemap.domain.project.dto.ProjectResponseDto;
import com.maptracker.issuemap.domain.project.entity.Project;
import com.maptracker.issuemap.domain.project.repository.ProjectRepository;
import com.maptracker.issuemap.domain.team.entity.Team;
import com.maptracker.issuemap.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;

    // 프로젝트 생성
    public ProjectResponseDto createProject(ProjectRequestDto projectRequestDto) {
        Team team = teamRepository.findById(projectRequestDto.getTeamId())
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        Project project = Project.builder()
                .team(team)
                .projectName(projectRequestDto.getProjectName())
                .createdAt(LocalDateTime.now())
                .build();

        Project savedProject = projectRepository.save(project);

        return new ProjectResponseDto(
                savedProject.getId(),
                savedProject.getProjectName(),
                savedProject.getCreatedAt(),
                savedProject.getTeam().getId(),
                savedProject.getTeam().getTeamName()
        );
    }

    // 프로젝트 조회
    public ProjectResponseDto getProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트를 찾을 수 없습니다."));
        return new ProjectResponseDto(
                project.getId(),
                project.getProjectName(),
                project.getCreatedAt(),
                project.getTeam().getId(),
                project.getTeam().getTeamName()
        );
    }
}
