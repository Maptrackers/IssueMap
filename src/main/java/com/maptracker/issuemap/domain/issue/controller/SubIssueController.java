package com.maptracker.issuemap.domain.issue.controller;


import com.maptracker.issuemap.domain.issue.dto.subissue.SubIssueResponseDto;
import com.maptracker.issuemap.domain.issue.dto.subissue.SubIssueCreateRequestDto;
import com.maptracker.issuemap.domain.issue.dto.subissue.SubIssueStatusUpdateRequestDto;
import com.maptracker.issuemap.domain.issue.dto.subissue.SubIssueUpdateRequestDto;
import com.maptracker.issuemap.domain.issue.service.SubIssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subissues")
@RequiredArgsConstructor
public class SubIssueController {

    private final SubIssueService subIssueService;

    @PostMapping
    public ResponseEntity<SubIssueCreateRequestDto> createSubIssue(
            @RequestBody SubIssueCreateRequestDto subIssueCreateRequestDto,
            @RequestParam Long userId) {
        SubIssueCreateRequestDto response = subIssueService.create(subIssueCreateRequestDto, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{subIssueId}")
    public ResponseEntity<SubIssueResponseDto> getSubIssueById(
            @PathVariable Long subIssueId
    ) {
        SubIssueResponseDto response = subIssueService.getSubIssueById(subIssueId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/issue/{issueId}")
    public ResponseEntity<List<SubIssueResponseDto>> getSubIssuesByIssue(
            @PathVariable Long issueId
    ) {
        List<SubIssueResponseDto> response = subIssueService.getSubIssueByIssue(issueId);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<SubIssueResponseDto> updateSubIssue(
            @RequestBody SubIssueUpdateRequestDto subIssueUpdateRequestDto
    ) {
        SubIssueResponseDto response = subIssueService.updateSubIssue(subIssueUpdateRequestDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping
    public ResponseEntity<SubIssueResponseDto> patchSubIssue(
            @RequestBody SubIssueStatusUpdateRequestDto subIssueStatusUpdateRequestDto
    ) {
        SubIssueResponseDto response = subIssueService.updateSubIssueStatus(subIssueStatusUpdateRequestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{subIssueId}")
    public ResponseEntity<SubIssueResponseDto> deleteSubIssue(
            @PathVariable Long subIssueId
    ) {
        SubIssueResponseDto response = subIssueService.deleteSubIssue(subIssueId);
        return ResponseEntity.ok(response);
    }
}
