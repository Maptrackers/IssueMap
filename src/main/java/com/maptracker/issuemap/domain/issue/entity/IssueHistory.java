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
public class IssueHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issuehistory_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id")
    private Issue issue;

    @Column(nullable = false)
    private String issueHistoryType;

    @Column(nullable = false)
    private String detail;

    @Builder
    private IssueHistory(User user, Issue issue, String issueHistoryType, String detail) {
        this.user = user;
        this.issue = issue;
        this.issueHistoryType = issueHistoryType;
        this.detail = detail;
    }

    public static IssueHistory create(User user, Issue issue, String issueHistoryType, String detail) {
        return IssueHistory.builder()
                .user(user)
                .issue(issue)
                .issueHistoryType(issueHistoryType)
                .detail(detail)
                .build();
    }

}
