package com.maptracker.issuemap.domain.comment.entity;


import com.maptracker.issuemap.common.global.BaseTimeEntity;
import com.maptracker.issuemap.domain.issue.entity.SubIssue;
import com.maptracker.issuemap.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubIssueComment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subissue_id", nullable = false)
    private SubIssue subIssue; // SubIssue와 연관

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private SubIssueComment parentComment; // 부모 댓글

    @OneToMany(mappedBy = "parentComment", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<SubIssueComment> childrenComment = new ArrayList<>(); // 자식 댓글들(대댓글)

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Builder
    public SubIssueComment(String content, User user, SubIssue subIssue, SubIssueComment parentComment) {
        this.content = content;
        this.user = user;
        this.subIssue = subIssue;
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