package com.maptracker.issuemap.domain.team.service;
import com.maptracker.issuemap.domain.team.entity.Team;
import com.maptracker.issuemap.domain.team.dto.TeamRequestDto;
import com.maptracker.issuemap.domain.team.dto.TeamResponseDto;
import com.maptracker.issuemap.domain.team.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;

    // 팀 생성
    public TeamResponseDto createTeam(TeamRequestDto teamRequestDto) {
        Team team = Team.builder()
                        .teamName(teamRequestDto.getTeamName())
                        .memberEmails(teamRequestDto.getMemberEmails())
                        .build();

        Team savedTeam = teamRepository.save(team);

        return TeamResponseDto.builder()
                .id(savedTeam.getId())
                .teamName(savedTeam.getTeamName())
                .memberEmails(savedTeam.getMemberEmails())
                .build();
    }

    // 팀 조회
    public TeamResponseDto getTeam(Long teamId){
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with id: " + teamId));

        return TeamResponseDto.builder()
                .id(team.getId())
                .teamName(team.getTeamName())
                .memberEmails(team.getMemberEmails())
                .build();
    }
}
