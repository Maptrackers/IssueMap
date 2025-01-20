package com.maptracker.issuemap.domain.comment.controller;


import com.maptracker.issuemap.common.jwt.CustomUserDetails;
import com.maptracker.issuemap.domain.comment.dto.IssueCommentCreateDto;
import com.maptracker.issuemap.domain.comment.dto.IssueCommentResponseDto;
import com.maptracker.issuemap.domain.comment.service.IssueCommentQueryService;
import com.maptracker.issuemap.domain.comment.service.IssueCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 스웨거도 인터페이스로??
@Slf4j
@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
@Tag(name = "IssueComment", description = "댓글 API")
public class IssueCommentController {
    private final IssueCommentService commentService;
    private final IssueCommentQueryService commentQueryService;

    @Operation(summary = "이슈 전체 댓글 조회", description = "해당 이슈ID 대한 전체 댓글을 조회한다")
    @GetMapping("/{issueId}/comments")
    public ResponseEntity<List<IssueCommentResponseDto>> getCommentsByIssue(
            @PathVariable Long issueId
    ) {
        log.info("댓글 조회 호출");
        List<IssueCommentResponseDto> comments = commentQueryService.getCommentsByIssue(issueId);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "댓글 생성", description = "해당 이슈ID 대한 댓글을 생성한다",
            parameters = {
            @Parameter(name = "issueId", description = "이슈 ID"),
//            @Parameter(name = "userId", description = "댓글을 작성하는 유저의 ID")
    })
    @PostMapping("/{issueId}/comments")
    public ResponseEntity<IssueCommentResponseDto> createComment(
            @PathVariable Long issueId,                      // PathVariable로 issueId를 받음
            @RequestBody IssueCommentCreateDto requestDto,
//            @RequestParam("userId") Long userId,         // QueryParam으로 userId를 받음
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        log.info("댓글 생성 호출");
        IssueCommentResponseDto responseDto = commentService.createComment(issueId, requestDto, userDetails);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "댓글 수정", description = "이슈ID,댓글ID 기준 해당 댓글을 수정한다")
    @PutMapping("/{issueId}/comments/{commentId}")
    public ResponseEntity<IssueCommentResponseDto> updateComment(
            @PathVariable Long issueId,
            @PathVariable Long commentId,
            @RequestBody IssueCommentCreateDto requestDto,
            @RequestParam Long userId
//            @AuthenticationPrincipal CustomUserDetails userDetails

    ) {
        log.info("댓글 수정 호출");
        IssueCommentResponseDto responseDto = commentService.updateComment(issueId, commentId, requestDto, userId);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "이슈ID,댓글ID 기준 해당 댓글 삭제", description = "이슈ID,댓글ID 기준 해당 댓글을 삭제한다")
    @DeleteMapping("/{issueId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long issueId,
            @PathVariable Long commentId,
            @RequestParam Long userId
//            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        log.info("댓글 삭제 호출");
        commentService.deleteComment(issueId, commentId, userId);
        return ResponseEntity.noContent().build();
    }

    // 대댓글 생성
    @Operation(summary = "대댓글 생성", description = "이슈ID,댓글ID 기준의 해당하는 댓글의 대댓글을 생성한다")
    @PostMapping("/{issueId}/comments/{parentCommentId}/replies")
    public ResponseEntity<IssueCommentResponseDto> createReply(
            @PathVariable Long issueId,
            @PathVariable Long parentCommentId,
            @RequestBody IssueCommentCreateDto requestDto,
            @RequestParam Long userId
//            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {// userId는 요청의 인증 정보에서 가져온다고 가정
        log.info("대댓글 생성 호출");
        IssueCommentResponseDto response = commentService.createReply(issueId, parentCommentId, requestDto, userId);
        return ResponseEntity.ok(response);
    }
}
