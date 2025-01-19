package com.maptracker.issuemap.domain.issue.service;

import com.maptracker.issuemap.domain.issue.dto.subissue.SubIssueCreateRequestDto;
import com.maptracker.issuemap.domain.issue.dto.subissue.SubIssueResponseDto;
import com.maptracker.issuemap.domain.issue.entity.Issue;
import com.maptracker.issuemap.domain.issue.entity.SubIssue;
import com.maptracker.issuemap.domain.issue.exception.IssueCustomException;
import com.maptracker.issuemap.domain.issue.exception.IssueErrorCode;
import com.maptracker.issuemap.domain.issue.exception.SubIssueCustomException;
import com.maptracker.issuemap.domain.issue.exception.SubIssueErrorCode;
import com.maptracker.issuemap.domain.issue.repository.IssueRepository;
import com.maptracker.issuemap.domain.issue.repository.SubIssueRepository;
import com.maptracker.issuemap.domain.user.entity.User;
import com.maptracker.issuemap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SubIssueService {

    private final UserRepository userRepository;
    private final IssueRepository issueRepository;
    private final SubIssueRepository subIssueRepository;

    public SubIssueCreateRequestDto create(SubIssueCreateRequestDto dto, Long userId) {

        User user = userRepository.findById(userId).orElseThrow();
        Issue issue = issueRepository.findById(dto.issueId()).orElseThrow();

        SubIssue subIssue = SubIssue.create(user, issue, dto.title());
        subIssueRepository.save(subIssue);

        return dto;
    }

    public SubIssueResponseDto getSubIssueById(Long subIssueId) {
        SubIssue subIssue = subIssueRepository.findById(subIssueId)
                .orElseThrow(() -> new SubIssueCustomException(SubIssueErrorCode.SUBISSUE_NOT_FOUND));;
        return SubIssueResponseDto.fromEntity(subIssue);
    }
}
