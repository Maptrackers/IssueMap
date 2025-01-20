package com.maptracker.issuemap.project.service;

import com.maptracker.issuemap.domain.project.dto.ProjectRequestDto;
import com.maptracker.issuemap.domain.project.dto.ProjectResponseDto;
import com.maptracker.issuemap.domain.project.dto.ProjectUpdateRequestDto;
import com.maptracker.issuemap.domain.project.entity.Project;
import com.maptracker.issuemap.domain.project.repository.ProjectRepository;
import com.maptracker.issuemap.domain.project.service.ProjectService;
import com.maptracker.issuemap.domain.team.entity.Team;
import com.maptracker.issuemap.domain.team.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @InjectMocks
    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TeamRepository teamRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        // projectRepository.save() 메서드 Mocking
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> {
            Project savedProject = invocation.getArgument(0);
            savedProject.setProjectId(1L);
            return savedProject;
        });
    }

    @Test
    void createProject() {
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .projectName("IssueMap")
                .teamId(1L)
                .build();

        Team team = Team.builder()
                .teamId(1L)
                .teamName("Maptrakers")
                .build();

        Project savedProject = Project.builder()
                .projectId(1L)
                .projectName("IssueMap")
                .createdAt(LocalDateTime.now())
                .team(team)
                .build();

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(projectRepository.save(any(Project.class))).thenReturn(savedProject);


        // When
        ProjectResponseDto result = projectService.createProject(projectRequestDto);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getProjectId());
        assertEquals("IssueMap", result.getProjectName());
        assertEquals(1L, result.getTeamId());
        assertEquals("Maptrakers", result.getTeamName());

        verify(teamRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void getProjectById() {
        // Given
        Team team = Team.builder()
                .teamName("Maptrakers")
                .build();

        Project project = Project.builder()
                .projectId(1L)
                .projectName("IssueMap")
                .createdAt(LocalDateTime.now())
                .team(team)
                .build();

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        // When
        ProjectResponseDto result = projectService.getProjectById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getProjectId());
        assertEquals("IssueMap", result.getProjectName());
        assertEquals("Maptrakers", result.getTeamName());

        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    void updateProject() {
        // Given
        ProjectUpdateRequestDto updateRequest = ProjectUpdateRequestDto.builder()
                .projectName("Updated Project")
                .build();

        Team team = Team.builder()
                .teamName("Maptrakers")
                .build();

        Project project = Project.builder()
                .projectId(1L)
                .projectName("IssueMap")
                .createdAt(LocalDateTime.now())
                .team(team)
                .build();

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        // When
        ProjectResponseDto result = projectService.updateProject(1L, updateRequest);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getProjectId());
        assertEquals("Updated Project", result.getProjectName());

        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void deleteProject() {
        // Given
        when(projectRepository.existsById(1L)).thenReturn(true);

        // When
        projectService.deleteProject(1L);

        // Then
        verify(projectRepository, times(1)).existsById(1L);
        verify(projectRepository, times(1)).deleteById(1L);
    }
}
