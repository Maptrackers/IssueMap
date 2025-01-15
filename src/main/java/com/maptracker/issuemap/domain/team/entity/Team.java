package com.maptracker.issuemap.domain.team.entity;
import com.maptracker.issuemap.domain.project.entity.Project;
import jakarta.persistence.*;
import lombok.Getter;
import java.util.List;

@Entity
@Getter
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @Column(name = "team_name", nullable = false)
    private String teamName;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects;
}
