package com.maptracker.issuemap.domain.comment.service;

import com.maptracker.issuemap.common.jwt.CustomUserDetails;
import com.maptracker.issuemap.domain.comment.dto.IssueCommentCreateDto;
import com.maptracker.issuemap.domain.comment.dto.IssueCommentResponseDto;
import com.maptracker.issuemap.domain.comment.entity.SubIssueComment;
import com.maptracker.issuemap.domain.comment.exception.MyErrorCode;
import com.maptracker.issuemap.domain.comment.exception.MyException;
import com.maptracker.issuemap.domain.comment.repository.SubIssueCommentRepository;
import com.maptracker.issuemap.domain.issue.entity.SubIssue;
import com.maptracker.issuemap.domain.issue.repository.SubIssueRepository;
import com.maptracker.issuemap.domain.user.entity.User;
import com.maptracker.issuemap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubIssueCommentService {
    private final SubIssueCommentRepository subIssueCommentRepository;
    private final SubIssueRepository subIssueRepository;
    private final UserRepository userRepository;


    @Transactional
    public IssueCommentResponseDto createComment(Long issueId, IssueCommentCreateDto requestDto, CustomUserDetails userDetails) {
        // 1. 이슈 조회
        SubIssue issue = subIssueRepository.findById(issueId).orElseThrow(() -> new MyException(MyErrorCode.ISSUE_NOT_FOUND));

        // 2. 유저 조회
//        User user = userRepository.findById(userId).orElseThrow(() -> new MyException(MyErrorCode.USER_NOT_FOUND));
        User user = userDetails.getUser();

        // 3. 부모 댓글 조회 (null이면 처음 상위 댓글)
        SubIssueComment parentComment = null;
        if (requestDto.getParentCommentId() != null) {
            parentComment = subIssueCommentRepository.findById(requestDto.getParentCommentId()).orElseThrow(() -> new MyException(MyErrorCode.COMMENT_NOT_FOUND));
        }

        SubIssueComment comment = SubIssueComment.builder().content(requestDto.getContent()).subIssue(issue).user(user).parentComment(parentComment).build();
        SubIssueComment savedComment = subIssueCommentRepository.save(comment);

        return IssueCommentResponseDto.builder()
                .id(savedComment.getId())
                .content(savedComment.getContent())
                .issueId(savedComment.getSubIssue().getId())
                .parentCommentId(parentComment != null ? parentComment.getId() : null)
                .userId(savedComment.getUser().getId())
                .build();
    }

    @Transactional
    public IssueCommentResponseDto updateComment(Long issueId, Long commentId, IssueCommentCreateDto requestDto, CustomUserDetails userDetails) {
        // 1. 댓글 조회
        SubIssueComment comment = subIssueCommentRepository.findById(commentId)
                .orElseThrow(() -> new MyException(MyErrorCode.COMMENT_NOT_FOUND));
        // 2. 이슈와 댓글 매칭 확인
        if (!comment.getSubIssue().getId().equals(issueId)) {
            throw new MyException(MyErrorCode.ISSUE_MISMATCH);
        }
        // 3. 유저와 댓글 작성자 매칭 확인
        User authenticatedUser = userDetails.getUser();
        if (!comment.getUser().getId().equals(authenticatedUser.getId())) {
            throw new MyException(MyErrorCode.UNAUTHORIZED_USER);
        }

        comment.updateContent(requestDto.getContent());
        return IssueCommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .issueId(comment.getSubIssue().getId())
                .userId(comment.getUser().getId())
                .build();
    }

    @Transactional
    public void deleteComment(Long issueId, Long commentId, CustomUserDetails userDetails) {
        // 1. 댓글 조회
        SubIssueComment comment = subIssueCommentRepository.findById(commentId).orElseThrow(() -> new MyException(MyErrorCode.COMMENT_NOT_FOUND));

        // 2. 이슈와 댓글 매칭 확인
        if (!comment.getSubIssue().getId().equals(issueId)) {
            throw new MyException(MyErrorCode.ISSUE_MISMATCH);
        }

        // 3. 유저와 댓글 작성자 매칭 확인
        User authenticatedUser = userDetails.getUser();
        if (!comment.getUser().getId().equals(authenticatedUser.getId())) {
            throw new MyException(MyErrorCode.UNAUTHORIZED_USER);
        }

        comment.markDeleted(); // Soft delete
        subIssueCommentRepository.save(comment);
    }


    // 대댓글 생성
    @Transactional
    public IssueCommentResponseDto createReply(Long issueId, Long parentCommentId, IssueCommentCreateDto requestDto, CustomUserDetails userDetails) {
        // 1. 이슈 조회 및 존재 여부 확인
        SubIssue issue = subIssueRepository.findById(issueId)
                .orElseThrow(() -> new MyException(MyErrorCode.ISSUE_NOT_FOUND));

        // 2. 유저 조회 및 존재 여부 확인
        User authenticatedUser = userDetails.getUser();
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new MyException(MyErrorCode.USER_NOT_FOUND));

        // 3. 부모 댓글 조회 및 존재 여부 확인
        SubIssueComment parentComment = subIssueCommentRepository.findById(parentCommentId)
                .orElseThrow(() -> new MyException(MyErrorCode.PARENT_COMMENT_NOT_FOUND));

        // 4. 부모 댓글이 해당 이슈에 속해 있는지 확인
        if (!parentComment.getSubIssue().getId().equals(issueId)) {
            throw new MyException(MyErrorCode.ISSUE_MISMATCH);
        }
        SubIssueComment reply = SubIssueComment.builder()
                .content(requestDto.getContent())
                .user(authenticatedUser)
                .subIssue(issue)
                .parentComment(parentComment)
                .build();

        SubIssueComment savedReply = subIssueCommentRepository.save(reply);

        return IssueCommentResponseDto.builder()
                .id(savedReply.getId())
                .content(savedReply.getContent())
                .userId(savedReply.getUser().getId())
                .issueId(savedReply.getSubIssue().getId())
                .parentCommentId(savedReply.getParentComment().getId())
                .build();
    }
}
