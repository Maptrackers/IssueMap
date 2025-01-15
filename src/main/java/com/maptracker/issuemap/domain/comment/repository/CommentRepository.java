package com.maptracker.issuemap.domain.comment.repository;

import com.maptracker.issuemap.domain.comment.entity.IssueComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<IssueComment, Long> {
}
