package com.maptracker.issuemap.domain.inquiry.service;

import com.maptracker.issuemap.domain.inquiry.dto.InquiryRequestDto;
import com.maptracker.issuemap.domain.inquiry.dto.ResponseRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final RestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8081/api/inquiries";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String INVALID_AUTH_HEADER = "Invalid Authorization header format.";

    // 문의를 생성합니다.
    public ResponseEntity<String> createInquiry(InquiryRequestDto request, String authorizationHeader) {
        HttpHeaders headers = createHeaders(authorizationHeader);
        HttpEntity<InquiryRequestDto> entity = new HttpEntity<>(request, headers);
        return restTemplate.exchange(BASE_URL, HttpMethod.POST, entity, String.class);
    }

    // 나머지 요청은 공통적으로 Authorization 처리 (Service 내부에서 처리)
    private HttpHeaders createHeaders(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            throw new IllegalArgumentException(INVALID_AUTH_HEADER);
        }
        String token = authorizationHeader.substring(BEARER_PREFIX.length());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", BEARER_PREFIX + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    // 전체 문의를 조회합니다.
    public ResponseEntity<String> getAllInquiries() {
        return restTemplate.exchange(BASE_URL, HttpMethod.GET, null, String.class);
    }

    // 특정 문의를 조회합니다.
    public ResponseEntity<String> getInquiryDetail(Long inquiryId) {
        return restTemplate.exchange(BASE_URL + "/" + inquiryId, HttpMethod.GET, null, String.class);
    }

    // 특정 문의를 삭제합니다.
    public ResponseEntity<String> deleteInquiry(Long inquiryId) {
        return restTemplate.exchange(BASE_URL + "/" + inquiryId, HttpMethod.DELETE, null, String.class);
    }

    // 특정 문의에 답변을 생성합니다.
    public ResponseEntity<String> createResponse(Long inquiryId, ResponseRequestDto request) {
        HttpEntity<ResponseRequestDto> entity = new HttpEntity<>(request);
        return restTemplate.exchange(BASE_URL + "/" + inquiryId + "/responses", HttpMethod.POST, entity, String.class);
    }

    // 특정 답변을 수정합니다.
    public ResponseEntity<String> updateResponse(Long inquiryId, Long responseId, ResponseRequestDto request) {
        HttpEntity<ResponseRequestDto> entity = new HttpEntity<>(request);
        return restTemplate.exchange(BASE_URL + "/" + inquiryId + "/responses/" + responseId,
                HttpMethod.PUT, entity, String.class);
    }

    // 특정 문의에 대한 모든 답변을 조회합니다.
    public ResponseEntity<String> getResponses(Long inquiryId) {
        return restTemplate.exchange(BASE_URL + "/" + inquiryId + "/responses", HttpMethod.GET, null, String.class);
    }

    // 특정 답변의 상태를 업데이트합니다.
    public ResponseEntity<String> updateStatus(Long inquiryId, Long responseId, ResponseRequestDto request) {
        HttpEntity<ResponseRequestDto> entity = new HttpEntity<>(request);
        return restTemplate.exchange(BASE_URL + "/" + inquiryId + "/responses/" + responseId + "/status",
                HttpMethod.PUT, entity, String.class);
    }
}