package com.maptracker.issuemap.domain.team.entity;
import com.maptracker.issuemap.domain.project.entity.Project;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "team_name", nullable = false)
    private String teamName;

    @ElementCollection(fetch = FetchType.EAGER) // 즉시 로딩으로 설정
    @CollectionTable(name = "team_member_emails", joinColumns = @JoinColumn(name = "team_id")) // 매핑 테이블 명시
    @Column(name = "member_email") // 컬럼 이름 지정
    private List<String> memberEmails;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects;

    @Builder
    public Team(Long teamId, String teamName, List<String> memberEmails) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.memberEmails = memberEmails != null ? memberEmails : new ArrayList<>();
    }
}
