package com.maptracker.issuemap.domain.issue.service;


import com.maptracker.issuemap.domain.issue.dto.issuehistory.IssueHistoryResponseDto;
import com.maptracker.issuemap.domain.issue.entity.IssueHistory;
import com.maptracker.issuemap.domain.issue.repository.IssueHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class IssueHistoryService {

    private final IssueHistoryRepository issueHistoryRepository;

    public List<IssueHistoryResponseDto> getIssuehistoryByIssue(Long issueId) {
        List<IssueHistory> issueHistories = issueHistoryRepository.findByIssueId(issueId);
        return issueHistories.stream()
                .map(IssueHistoryResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
