package com.maptracker.issuemap.domain.team.entity;
import com.maptracker.issuemap.domain.project.entity.Project;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @Column(name = "team_name", nullable = false)
    private String teamName;

    @ElementCollection(fetch = FetchType.EAGER) // 즉시 로딩으로 설정
    @CollectionTable(name = "team_member_emails", joinColumns = @JoinColumn(name = "team_id")) // 매핑 테이블 명시
    @Column(name = "member_email") // 컬럼 이름 지정
    private List<String> memberEmails;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects;

    @Builder
    public Team(String teamName, List<String> memberEmails) {
        this.teamName = teamName;
        this.memberEmails = memberEmails != null ? memberEmails : new ArrayList<>();
    }
}
