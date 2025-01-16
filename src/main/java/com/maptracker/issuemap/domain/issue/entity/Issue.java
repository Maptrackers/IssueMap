package com.maptracker.issuemap.domain.issue.entity;

import com.maptracker.issuemap.common.global.BaseTimeEntity;
import com.maptracker.issuemap.domain.project.entity.Project;
import com.maptracker.issuemap.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Issue extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Issue_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(nullable = false)
    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private IssueType issueType;

    private String status;

    @Builder
    private Issue(User user, Project project, String title, String content, IssueType issueType) {
        this.user = user;
        this.project = project;
        this.title = title;
        this.content = content;
        this.issueType = issueType;
    }

    public static Issue create(User user, Project project, String title, String issuetype) {
        return Issue.builder()
                .user(user)
                .project(project)
                .title(title)
                .content(null)
                .issueType(IssueType.valueOf(issuetype.toUpperCase()))
                .build();
    }

}
