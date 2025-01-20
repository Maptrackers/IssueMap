package com.maptracker.issuemap.team.service;

import com.maptracker.issuemap.domain.team.dto.TeamRequestDto;
import com.maptracker.issuemap.domain.team.dto.TeamResponseDto;
import com.maptracker.issuemap.domain.team.entity.Team;
import com.maptracker.issuemap.domain.team.repository.TeamRepository;
import com.maptracker.issuemap.domain.team.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TeamServiceTest {

    @InjectMocks
    private TeamService teamService; // 테스트할 대상 클래스

    @Mock
    private TeamRepository teamRepository; // Mock 객체

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        // createTeam과 updateTeam에서 중복되어 @BeforeEach에서 설정
        // teamRepository.save() 메서드의 동작을 Mocking
        when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> {
            Team savedTeam = invocation.getArgument(0);
            savedTeam.setTeamId(1L); // Mock으로 ID 설정
            return savedTeam;
        });
    }

    @Test
    void createTeam() {
        // Given - 테스트 환경을 설정하는 단계
        TeamRequestDto teamRequestDto = TeamRequestDto.builder()
                .teamName("Maptrakers")
                .memberEmails(Arrays.asList("member1@example.com", "member2@example.com"))
                .build();
        Team savedTeam = Team.builder()
                .teamName("Maptrakers")
                .memberEmails(Arrays.asList("member1@example.com", "member2@example.com"))
                .build();

        // When - 테스트 대상 호출
        TeamResponseDto result = teamService.createTeam(teamRequestDto);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getTeamId());
        assertEquals("Maptrakers", result.getTeamName());
        assertEquals(Arrays.asList("member1@example.com", "member2@example.com"), result.getMemberEmails());

        verify(teamRepository, times(1)).save(any(Team.class)); // save 메서드가 1번 호출됐는지 검증

    }

    @Test
    void getTeam() {
        // Given
        Team team = Team.builder()
                .teamName("Maptrakers")
                .memberEmails(Arrays.asList("member1@example.com", "member2@example.com"))
                .build();

        when(teamRepository.findById(1L)).thenAnswer(invocation -> {
            team.setTeamId(1L);
            return Optional.of(team);
        });

        // When
        TeamResponseDto result = teamService.getTeam(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getTeamId());
        assertEquals("Maptrakers", result.getTeamName());
        assertEquals(Arrays.asList("member1@example.com", "member2@example.com"), result.getMemberEmails());

        verify(teamRepository, times(1)).findById(1L);
    }

    @Test
    void updateTeam() {
        // Given
        Team existingTeam = Team.builder()
                .teamName("Maptrakers")
                .memberEmails(Arrays.asList("member1@example.com", "member2@example.com"))
                .build();

        TeamRequestDto updateRequest = TeamRequestDto.builder()
                .teamName("Updated Team")
                .memberEmails(Arrays.asList("member3@example.com", "member4@example.com"))
                .build();

        when(teamRepository.findById(1L)).thenAnswer(invocation -> {
            existingTeam.setTeamId(1L);
            return Optional.of(existingTeam);
        });

        // When
        TeamResponseDto result = teamService.updateTeam(1L, updateRequest);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getTeamId());
        assertEquals("Updated Team", result.getTeamName());
        assertEquals(Arrays.asList("member3@example.com", "member4@example.com"), result.getMemberEmails());

        verify(teamRepository, times(1)).findById(1L);
        verify(teamRepository, times(1)).save(existingTeam);
    }

    @Test
    void deleteTeam() {
        // Given
        when(teamRepository.existsById(1L)).thenReturn(true);

        // When
        teamService.deleteTeam(1L);

        // Then
        verify(teamRepository, times(1)).existsById(1L);
        verify(teamRepository, times(1)).deleteById(1L);
    }
}
