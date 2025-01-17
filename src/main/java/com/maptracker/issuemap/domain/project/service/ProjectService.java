package com.maptracker.issuemap.domain.project.service;

import com.maptracker.issuemap.domain.project.dto.ProjectRequestDto;
import com.maptracker.issuemap.domain.project.dto.ProjectResponseDto;
import com.maptracker.issuemap.domain.project.dto.ProjectUpdateRequestDto;
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

        return ProjectResponseDto.builder()
                .id(savedProject.getId())
                .projectName(savedProject.getProjectName())
                .createdAt(savedProject.getCreatedAt())
                .teamId(savedProject.getTeam().getId())
                .teamName(savedProject.getTeam().getTeamName())
                .build();
    }

    // 프로젝트 조회
    public ProjectResponseDto getProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트를 찾을 수 없습니다."));
        return ProjectResponseDto.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .createdAt(project.getCreatedAt())
                .teamId(project.getTeam().getId())
                .teamName(project.getTeam().getTeamName())
                .build();
    }

    // 프로젝트 수정
    public ProjectResponseDto updateProject(Long projectId, ProjectUpdateRequestDto projectUpdateRequestDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        // 수정 로직
        project.setProjectName(projectUpdateRequestDto.getProjectName());

        Project updatedProject = projectRepository.save(project);

        return ProjectResponseDto.builder()
                .id(updatedProject.getId())
                .projectName(updatedProject.getProjectName())
                .createdAt(updatedProject.getCreatedAt())
                .teamId(updatedProject.getTeam().getId())
                .teamName(updatedProject.getTeam().getTeamName())
                .build();
    }

    // 프로젝트 삭제
    public void deleteProject(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new IllegalArgumentException("Project not found with id: " + projectId);
        }
        projectRepository.deleteById(projectId);
    }
}
