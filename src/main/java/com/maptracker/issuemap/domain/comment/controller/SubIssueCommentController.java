package com.maptracker.issuemap.domain.comment.controller;

import com.maptracker.issuemap.common.jwt.CustomUserDetails;
import com.maptracker.issuemap.domain.comment.dto.IssueCommentCreateDto;
import com.maptracker.issuemap.domain.comment.dto.IssueCommentResponseDto;
import com.maptracker.issuemap.domain.comment.service.SubIssueCommentService;
import com.maptracker.issuemap.domain.comment.service.SubIssueCommentQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/subissues")
@RequiredArgsConstructor
@Tag(name = "SubIssueComment", description = "서브 이슈 댓글 API")
public class SubIssueCommentController {

    private final SubIssueCommentService commentService;
    private final SubIssueCommentQueryService commentQueryService;

    @Operation(summary = "서브 이슈 전체 댓글 조회", description = "해당 서브 이슈 ID에 대한 전체 댓글을 조회한다")
    @GetMapping("/{subIssueId}/comments")
    public ResponseEntity<List<IssueCommentResponseDto>> getCommentsBySubIssue(
            @PathVariable Long subIssueId
    ) {
        log.info("서브 이슈 댓글 조회 호출");
        List<IssueCommentResponseDto> comments = commentQueryService.getCommentsBySubIssue(subIssueId);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "서브 이슈 댓글 생성", description = "해당 서브 이슈 ID에 대한 댓글을 생성한다",
            parameters = {
                    @Parameter(name = "subIssueId", description = "서브 이슈 ID"),
                    @Parameter(name = "userId", description = "댓글을 작성하는 유저의 ID")
            })
    @PostMapping("/{subIssueId}/comments")
    public ResponseEntity<IssueCommentResponseDto> createComment(
            @PathVariable Long subIssueId,
            @RequestBody IssueCommentCreateDto requestDto,
//            @RequestParam("userId") Long userId
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        log.info("서브 이슈 댓글 생성 호출");
        IssueCommentResponseDto responseDto = commentService.createComment(subIssueId, requestDto, userDetails);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "서브 이슈 댓글 수정", description = "서브 이슈 ID와 댓글 ID를 기준으로 해당 댓글을 수정한다")
    @PutMapping("/{subIssueId}/comments/{commentId}")
    public ResponseEntity<IssueCommentResponseDto> updateComment(
            @PathVariable Long subIssueId,
            @PathVariable Long commentId,
            @RequestBody IssueCommentCreateDto requestDto,
//            @RequestParam Long userId
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        log.info("서브 이슈 댓글 수정 호출");
        IssueCommentResponseDto responseDto = commentService.updateComment(subIssueId, commentId, requestDto, userDetails);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "서브 이슈 댓글 삭제", description = "서브 이슈 ID와 댓글 ID를 기준으로 해당 댓글을 삭제한다")
    @DeleteMapping("/{subIssueId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long subIssueId,
            @PathVariable Long commentId,
//            @RequestParam Long userId
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        log.info("서브 이슈 댓글 삭제 호출");
        commentService.deleteComment(subIssueId, commentId, userDetails);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "서브 이슈 대댓글 생성", description = "서브 이슈 ID와 댓글 ID를 기준으로 대댓글을 생성한다")
    @PostMapping("/{subIssueId}/comments/{parentCommentId}/replies")
    public ResponseEntity<IssueCommentResponseDto> createReply(
            @PathVariable Long subIssueId,
            @PathVariable Long parentCommentId,
            @RequestBody IssueCommentCreateDto requestDto,
            @RequestParam Long userId
    ) {
        log.info("서브 이슈 대댓글 생성 호출");
        IssueCommentResponseDto response = commentService.createReply(subIssueId, parentCommentId, requestDto, userId);
        return ResponseEntity.ok(response);
    }
}