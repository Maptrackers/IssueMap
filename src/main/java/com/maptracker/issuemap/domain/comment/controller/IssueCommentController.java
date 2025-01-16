package com.maptracker.issuemap.domain.comment.controller;


import com.maptracker.issuemap.domain.comment.dto.IssueCommentRequestDto;
import com.maptracker.issuemap.domain.comment.dto.IssueCommentResponseDto;
import com.maptracker.issuemap.domain.comment.service.IssueCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class IssueCommentController {
    private final IssueCommentService commentService;

    @PostMapping("/{issueId}/comments")
    public ResponseEntity<IssueCommentResponseDto> createComment(
            @PathVariable Long issueId,                      // PathVariable로 issueId를 받음
            @RequestBody IssueCommentRequestDto requestDto,
            @RequestParam Long userId                        // QueryParam으로 userId를 받음
    ) {
        IssueCommentResponseDto responseDto = commentService.createComment(issueId, requestDto, userId);
        return ResponseEntity.ok(responseDto);
    }

}