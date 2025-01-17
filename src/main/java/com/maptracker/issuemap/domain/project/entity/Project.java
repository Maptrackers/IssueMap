package com.maptracker.issuemap.domain.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import com.maptracker.issuemap.domain.team.entity.Team;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

}
