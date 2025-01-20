package com.maptracker.issuemap.domain.comment.service;

import com.maptracker.issuemap.domain.comment.dto.IssueCommentResponseDto;
import com.maptracker.issuemap.domain.comment.entity.SubIssueComment;
import com.maptracker.issuemap.domain.comment.repository.IssueCommentRepository;
import com.maptracker.issuemap.domain.comment.repository.SubIssueCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubIssueCommentQueryService {
    private final SubIssueCommentRepository subIssueCommentRepository;

    @Transactional(readOnly = true)
    public List<IssueCommentResponseDto> getCommentsBySubIssue(Long subIssueID) {
        // 이슈 ID로 댓글 조회 (삭제되지 않은 댓글만 가져옴)
        List<SubIssueComment> comments = subIssueCommentRepository.findAllBySubIssueIdAndIsDeletedFalse(subIssueID);

        // 댓글 목록을 DTO로 변환
        return comments.stream()
                .map(comment -> IssueCommentResponseDto.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .issueId(comment. getSubIssue().getId())
                        .userId(comment.getUser().getId())
                        .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                        .build())
                .toList();
    }
}
