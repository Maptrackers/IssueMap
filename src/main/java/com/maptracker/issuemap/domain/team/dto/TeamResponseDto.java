package com.maptracker.issuemap.domain.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamResponseDto {

    @Schema(description = "팀 ID", example = "1")
    private Long teamId;

    @Schema(description = "팀 이름", example = "MapTrackers")
    private String teamName;

    @Schema(
            description = "팀 멤버 이메일 목록",
            example = "[\"member1@example.com\", \"member2@example.com\"]"
    )
    private List<String> memberEmails;
}
