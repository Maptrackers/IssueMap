package com.maptracker.issuemap.domain.comment.controller;


import com.maptracker.issuemap.domain.comment.dto.IssueCommentRequestDto;
import com.maptracker.issuemap.domain.comment.dto.IssueCommentResponseDto;
import com.maptracker.issuemap.domain.comment.service.IssueCommentQueryService;
import com.maptracker.issuemap.domain.comment.service.IssueCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class IssueCommentController {
    private final IssueCommentService commentService;
    private final IssueCommentQueryService commentQueryService;

    @GetMapping("/{issueId}/comments")
    public ResponseEntity<List<IssueCommentResponseDto>> getCommentsByIssue(
            @PathVariable Long issueId
    ) {
        List<IssueCommentResponseDto> comments = commentQueryService.getCommentsByIssue(issueId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{issueId}/comments")
    public ResponseEntity<IssueCommentResponseDto> createComment(
            @PathVariable Long issueId,                      // PathVariable로 issueId를 받음
            @RequestBody IssueCommentRequestDto requestDto,
            @RequestParam Long userId                        // QueryParam으로 userId를 받음
    ) {
        IssueCommentResponseDto responseDto = commentService.createComment(issueId, requestDto, userId);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{issueId}/comments/{commentId}")
    public ResponseEntity<IssueCommentResponseDto> updateComment(
            @PathVariable Long issueId,
            @PathVariable Long commentId,
            @RequestBody IssueCommentRequestDto requestDto,
            @RequestParam Long userId
    ) {
        IssueCommentResponseDto responseDto = commentService.updateComment(issueId, commentId, requestDto, userId);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{issueId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long issueId,
            @PathVariable Long commentId,
            @RequestParam Long userId
    ) {
        commentService.deleteComment(issueId, commentId, userId);
        return ResponseEntity.noContent().build();
    }

    // 대댓글 생성
    @PostMapping("/{issueId}/comments/{parentCommentId}/replies")
    public ResponseEntity<IssueCommentResponseDto> createReply(
            @PathVariable Long issueId,
            @PathVariable Long parentCommentId,
            @RequestBody IssueCommentRequestDto requestDto,
            @RequestParam Long userId) {  // userId는 요청의 인증 정보에서 가져온다고 가정
        IssueCommentResponseDto response = commentService.createReply(issueId, parentCommentId, requestDto, userId);
        return ResponseEntity.ok(response);
    }
}
