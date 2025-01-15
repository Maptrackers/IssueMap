package com.maptracker.issuemap.domain.comment.service;

import com.maptracker.issuemap.domain.comment.dto.IssueCommentRequestDto;
import com.maptracker.issuemap.domain.comment.dto.IssueCommentResponseDto;
import com.maptracker.issuemap.domain.comment.entity.IssueComment;
import com.maptracker.issuemap.domain.comment.exception.MyErrorCode;
import com.maptracker.issuemap.domain.comment.exception.MyException;
import com.maptracker.issuemap.domain.comment.repository.CommentRepository;
import com.maptracker.issuemap.domain.issue.entity.Issue;
import com.maptracker.issuemap.domain.issue.repository.IssueRepository;
import com.maptracker.issuemap.domain.user.entity.User;
import com.maptracker.issuemap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IssueCommentService {
    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;

    @Transactional
    public IssueCommentResponseDto createComment(IssueCommentRequestDto requestDto, Long userId) {
        // 1. 이슈 조회
        Issue issue = issueRepository.findById(requestDto.getIssueId()).orElseThrow(() -> new MyException(MyErrorCode.ISSUE_NOT_FOUND));
        // 2. 유저 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new MyException(MyErrorCode.USER_NOT_FOUND));
        // 3. 부모 댓글 조회 (null이면 처음 상위 댓글)
        IssueComment parentComment = null;
        if (requestDto.getParentCommentId() != null) {
            parentComment = commentRepository.findById(requestDto.getParentCommentId()).orElseThrow(() -> new MyException(MyErrorCode.COMMENT_NOT_FOUND));
        }

        IssueComment comment = IssueComment.builder().content(requestDto.getContent()).issue(issue).user(user).parentComment(parentComment).build();
        IssueComment savedComment = commentRepository.save(comment);

        return IssueCommentResponseDto.builder()
                .id(savedComment.getId())
                .content(savedComment.getContent())
                .issueId(savedComment.getIssue().getId())
                .parentCommentId(parentComment != null ? parentComment.getId() : null)
                .userId(savedComment.getUser().getId())
                .build();
    }
}
