package com.maptracker.issuemap.domain.comment.repository;

import com.maptracker.issuemap.domain.comment.entity.SubIssueComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubIssueCommentRepository extends JpaRepository<SubIssueComment, Long> {
    List<SubIssueComment> findAllBySubIssueIdAndIsDeletedFalse(Long subIssueId);

}
