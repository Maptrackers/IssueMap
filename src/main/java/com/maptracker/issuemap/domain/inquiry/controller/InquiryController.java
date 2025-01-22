package com.maptracker.issuemap.domain.inquiry.controller;

import com.maptracker.issuemap.domain.inquiry.dto.InquiryRequestDto;
import com.maptracker.issuemap.domain.inquiry.dto.ResponseRequestDto;
import com.maptracker.issuemap.domain.inquiry.service.InquiryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    @Operation(summary = "문의 작성", description = "새로운 문의를 작성합니다.")
    @PostMapping
    public ResponseEntity<String> createInquiry(
            @RequestBody InquiryRequestDto request,
            @RequestHeader("Authorization") String authorizationHeader) {

        return inquiryService.createInquiry(request, authorizationHeader);
    }

    @Operation(summary = "전체 문의 조회", description = "전체 문의를 조회합니다.")
    @GetMapping
    public ResponseEntity<String> getAllInquiries() {
        return inquiryService.getAllInquiries();
    }

    @Operation(summary = "특정 문의 조회", description = "특정 문의를 조회합니다.")
    @GetMapping("/{inquiryId}")
    public ResponseEntity<String> getInquiryDetail(@PathVariable Long inquiryId) {
        return inquiryService.getInquiryDetail(inquiryId);
    }

    @Operation(summary = "문의 삭제", description = "문의를 삭제합니다.")
    @DeleteMapping("/{inquiryId}")
    public ResponseEntity<String> deleteInquiry(@PathVariable Long inquiryId) {
        return inquiryService.deleteInquiry(inquiryId);
    }

    @Operation(summary = "답변 작성", description = "문의 답변을 작성합니다.")
    @PostMapping("/{inquiryId}/responses")
    public ResponseEntity<String> createResponse(
            @PathVariable Long inquiryId,
            @RequestBody ResponseRequestDto request) {

        return inquiryService.createResponse(inquiryId, request);
    }

    @Operation(summary = "답변 수정", description = "문의 답변을 수정합니다.")
    @PutMapping("/{inquiryId}/responses/{responseId}")
    public ResponseEntity<String> updateResponse(
            @PathVariable Long inquiryId,
            @PathVariable Long responseId,
            @RequestBody ResponseRequestDto request) {

        return inquiryService.updateResponse(inquiryId, responseId, request);
    }

    @Operation(summary = "답변 조회", description = "문의 답변을 조회합니다.")
    @GetMapping("/{inquiryId}/responses")
    public ResponseEntity<String> getResponses(@PathVariable Long inquiryId) {
        return inquiryService.getResponses(inquiryId);
    }

    @Operation(summary = "답변 상태 변경", description = "문의 답변 상태를 변경합니다.")
    @PatchMapping("/{inquiryId}/responses/{responseId}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable Long inquiryId,
            @PathVariable Long responseId,
            @RequestBody ResponseRequestDto request) {

        return inquiryService.updateStatus(inquiryId, responseId, request);
    }
}