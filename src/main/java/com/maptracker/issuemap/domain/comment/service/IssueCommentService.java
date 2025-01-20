package com.maptracker.issuemap.domain.comment.service;

import com.maptracker.issuemap.common.jwt.CustomUserDetails;
import com.maptracker.issuemap.domain.comment.dto.IssueCommentCreateDto;
import com.maptracker.issuemap.domain.comment.dto.IssueCommentResponseDto;
import com.maptracker.issuemap.domain.comment.entity.IssueComment;
import com.maptracker.issuemap.domain.comment.exception.MyErrorCode;
import com.maptracker.issuemap.domain.comment.exception.MyException;
import com.maptracker.issuemap.domain.comment.repository.IssueCommentRepository;
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
    private final IssueCommentRepository commentRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;

    @Transactional
    public IssueCommentResponseDto createComment(Long issueId, IssueCommentCreateDto requestDto, CustomUserDetails userDetails) {
        // 1. 이슈 조회
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new MyException(MyErrorCode.ISSUE_NOT_FOUND));

        // 2. 유저 조회
        User user = userDetails.getUser();
//        User user = userRepository.findById(userId).orElseThrow(() -> new MyException(MyErrorCode.USER_NOT_FOUND));

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

    @Transactional
    public IssueCommentResponseDto updateComment(Long issueId, Long commentId, IssueCommentCreateDto requestDto, Long userId) {
        // 1. 댓글 조회
        IssueComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new MyException(MyErrorCode.COMMENT_NOT_FOUND));
        // 2. 이슈와 댓글 매칭 확인
        if (!comment.getIssue().getId().equals(issueId)) {
            throw new MyException(MyErrorCode.ISSUE_MISMATCH);
        }
        // 3. 유저와 댓글 작성자 매칭 확인
        if (!comment.getUser().getId().equals(userId)) {
            throw new MyException(MyErrorCode.UNAUTHORIZED_USER);
        }

        comment.updateContent(requestDto.getContent());
        return IssueCommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .issueId(comment.getIssue().getId())
                .userId(comment.getUser().getId())
                .build();
    }

    @Transactional
    public void deleteComment(Long issueId, Long commentId, Long userId) {
        // 1. 댓글 조회
        IssueComment comment = commentRepository.findById(commentId).orElseThrow(() -> new MyException(MyErrorCode.COMMENT_NOT_FOUND));

        // 2. 이슈와 댓글 매칭 확인
        if (!comment.getIssue().getId().equals(issueId)) {
            throw new MyException(MyErrorCode.ISSUE_MISMATCH);
        }

        // 3. 유저와 댓글 작성자 매칭 확인
        if (!comment.getUser().getId().equals(userId)) {
            throw new MyException(MyErrorCode.UNAUTHORIZED_USER);
        }

        comment.markDeleted(); // Soft delete
        commentRepository.save(comment);
    }


    // 대댓글 생성
    @Transactional
    public IssueCommentResponseDto createReply(Long issueId, Long parentCommentId, IssueCommentCreateDto requestDto, Long userId) {
        // 1. 이슈 조회 및 존재 여부 확인
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new MyException(MyErrorCode.ISSUE_NOT_FOUND));

        // 2. 유저 조회 및 존재 여부 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new MyException(MyErrorCode.USER_NOT_FOUND));

        // 3. 부모 댓글 조회 및 존재 여부 확인
        IssueComment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new MyException(MyErrorCode.PARENT_COMMENT_NOT_FOUND));

        // 4. 부모 댓글이 해당 이슈에 속해 있는지 확인
        if (!parentComment.getIssue().getId().equals(issueId)) {
            throw new MyException(MyErrorCode.ISSUE_MISMATCH);
        }
        IssueComment reply = IssueComment.builder()
                .content(requestDto.getContent())
                .user(user)
                .issue(issue)
                .parentComment(parentComment)
                .build();

        IssueComment savedReply = commentRepository.save(reply);

        return IssueCommentResponseDto.builder()
                .id(savedReply.getId())
                .content(savedReply.getContent())
                .userId(savedReply.getUser().getId())
                .issueId(savedReply.getIssue().getId())
                .parentCommentId(savedReply.getParentComment().getId())
                .build();
    }
}
