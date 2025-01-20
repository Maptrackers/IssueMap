package com.maptracker.issuemap.domain.comment.repository;

import com.maptracker.issuemap.domain.comment.entity.IssueComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueCommentRepository extends JpaRepository<IssueComment, Long> {
    // 이슈 ID를 기준으로 삭제되지 않은 댓글 조회
    List<IssueComment> findAllByIssueIdAndIsDeletedFalse(Long issueId);
}
