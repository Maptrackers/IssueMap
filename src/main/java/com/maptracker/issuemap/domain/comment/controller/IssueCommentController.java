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
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class IssueCommentController {
    private final IssueCommentService commentService;

    @PostMapping
    public ResponseEntity<IssueCommentResponseDto> createComment(
            @RequestBody IssueCommentRequestDto requestDto,
            @RequestParam Long userId
    ) {
        IssueCommentResponseDto responseDto = commentService.createComment(requestDto, userId);
        return ResponseEntity.ok(responseDto);
    }

}
