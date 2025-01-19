package com.maptracker.issuemap.domain.issue.entity;

import com.maptracker.issuemap.common.global.BaseTimeEntity;
import com.maptracker.issuemap.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubIssue extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subissue_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id")
    private Issue issue;

    @Column(nullable = false)
    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private IssueStatus issueStatus;

    @Builder
    private SubIssue(User user, Issue issue, String title, String content, IssueStatus issueStatus) {
        this.user = user;
        this.issue = issue;
        this.title = title;
        this.content = content;
        this.issueStatus = issueStatus;
    }

    public static SubIssue create(User user, Issue issue, String title) {
        return SubIssue.builder()
                .user(user)
                .issue(issue)
                .title(title)
                .build();
    }


    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
