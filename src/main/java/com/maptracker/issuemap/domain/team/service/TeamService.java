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
                .teamId(savedTeam.getTeamId())
                .teamName(savedTeam.getTeamName())
                .memberEmails(savedTeam.getMemberEmails())
                .build();
    }

    // 팀 조회
    public TeamResponseDto getTeam(Long teamId){
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with id: " + teamId));

        return TeamResponseDto.builder()
                .teamId(team.getTeamId())
                .teamName(team.getTeamName())
                .memberEmails(team.getMemberEmails())
                .build();
    }

    // 팀 수정
    public TeamResponseDto updateTeam(Long teamId, TeamRequestDto teamRequestDto) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with id: " + teamId));

        // 수정 로직
        team.setTeamName(teamRequestDto.getTeamName());
        team.setMemberEmails(teamRequestDto.getMemberEmails());

        Team updatedTeam = teamRepository.save(team); // 저장 후 반환

        return TeamResponseDto.builder()
                .teamId(updatedTeam.getTeamId())
                .teamName(updatedTeam.getTeamName())
                .memberEmails(updatedTeam.getMemberEmails())
                .build();
    }

    // 팀 삭제
    public void deleteTeam(Long teamId) {
        if (!teamRepository.existsById(teamId)) {
            throw new IllegalArgumentException("Team not found with id: " + teamId);
        }
        teamRepository.deleteById(teamId);
    }



}
