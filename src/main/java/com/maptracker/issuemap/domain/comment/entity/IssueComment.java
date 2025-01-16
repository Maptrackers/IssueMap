package com.maptracker.issuemap.domain.comment.entity;

import com.maptracker.issuemap.common.global.BaseTimeEntity;
import com.maptracker.issuemap.domain.issue.entity.Issue;
import com.maptracker.issuemap.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IssueComment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id")
    private Issue issue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private IssueComment parentComment; //부모 댓글

    @OneToMany(mappedBy = "parentComment", orphanRemoval = true)
    private List<IssueComment> childrenComment = new ArrayList<>(); //자식 댓글들(대댓글)

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Builder
    public IssueComment(String content, User user, Issue issue, IssueComment parentComment) {
        this.content = content;
        this.user = user;
        this.issue = issue;
        this.parentComment = parentComment;
        this.isDeleted = false;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void markDeleted() {
        this.isDeleted = true;
    }
}
