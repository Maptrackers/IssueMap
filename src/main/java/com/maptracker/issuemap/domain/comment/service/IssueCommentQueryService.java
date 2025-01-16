package com.maptracker.issuemap.domain.comment.service;


import com.maptracker.issuemap.domain.comment.dto.IssueCommentResponseDto;
import com.maptracker.issuemap.domain.comment.entity.IssueComment;
import com.maptracker.issuemap.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueCommentQueryService {
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public List<IssueCommentResponseDto> getCommentsByIssue(Long issueId) {
        // 이슈 ID로 댓글 조회 (삭제되지 않은 댓글만 가져옴)
        List<IssueComment> comments = commentRepository.findAllByIssueIdAndIsDeletedFalse(issueId);

        // 댓글 목록을 DTO로 변환
        return comments.stream()
                .map(comment -> IssueCommentResponseDto.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .issueId(comment.getIssue().getId())
                        .userId(comment.getUser().getId())
                        .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                        .build())
                .toList();
    }

}
