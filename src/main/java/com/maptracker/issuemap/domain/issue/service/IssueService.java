package com.maptracker.issuemap.domain.issue.service;

import com.maptracker.issuemap.domain.issue.dto.issue.IssueCreateRequestDto;
import com.maptracker.issuemap.domain.issue.dto.issue.IssueResponseDto;
import com.maptracker.issuemap.domain.issue.dto.issue.IssueStatusUpdateRequestDto;
import com.maptracker.issuemap.domain.issue.dto.issue.IssueUpdateRequestDto;
import com.maptracker.issuemap.domain.issue.entity.Issue;
import com.maptracker.issuemap.domain.issue.entity.IssueStatus;
import com.maptracker.issuemap.domain.issue.entity.IssueType;
import com.maptracker.issuemap.domain.issue.exception.IssueCustomException;
import com.maptracker.issuemap.domain.issue.exception.IssueErrorCode;
import com.maptracker.issuemap.domain.issue.repository.IssueRepository;
import com.maptracker.issuemap.domain.project.entity.Project;
import com.maptracker.issuemap.domain.project.repository.ProjectRepository;
import com.maptracker.issuemap.domain.user.entity.User;
import com.maptracker.issuemap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class IssueService {

    private final UserRepository userRepository;
    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;

    public IssueCreateRequestDto createIssue(IssueCreateRequestDto dto, Long userId) {

        // 나중에 에러처리 추가
        User user = userRepository.findById(userId).orElseThrow();
        Project project = projectRepository.findById(dto.projectId()).orElseThrow();

        try {
            Issue issue = Issue.create(user, project, dto.title(), dto.issueType());
            issueRepository.save(issue);
        } catch (IllegalArgumentException e) {
            throw new IssueCustomException(IssueErrorCode.INVALID_ISSUE_TYPE);
        }

        return dto;
    }

    public IssueResponseDto getIssueById(Long issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueCustomException(IssueErrorCode.ISSUE_NOT_FOUND));
        return IssueResponseDto.fromEntity(issue);
    }

    public List<IssueResponseDto> getIssuesByProject(Long projectId) {
        List<Issue> issues = issueRepository.findByProjectId(projectId);
        return issues.stream()
                .map(IssueResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public IssueResponseDto updateIssue(IssueUpdateRequestDto dto) {
        Issue issue = issueRepository.findById(dto.issueId())
                .orElseThrow(() -> new IssueCustomException(IssueErrorCode.ISSUE_NOT_FOUND));

        if (dto.title() != null && !dto.title().isBlank()) {
            issue.updateTitle(dto.title());
        }
        if (dto.content() != null) {
            issue.updateContent(dto.content());
        }
        if (dto.issueType() != null) {
            try {
                IssueType issueType = IssueType.valueOf(dto.issueType().toUpperCase());
                issue.updateIssueType(issueType);
            } catch (IllegalArgumentException e) {
                throw new IssueCustomException(IssueErrorCode.INVALID_ISSUE_TYPE);
            }
        }

        issueRepository.save(issue);
        return IssueResponseDto.fromEntity(issue);
    }

    public IssueResponseDto updateIssueStatus(IssueStatusUpdateRequestDto dto) {
        Issue issue = issueRepository.findById(dto.issueId())
                .orElseThrow(() -> new IssueCustomException(IssueErrorCode.ISSUE_NOT_FOUND));

        try {
            IssueStatus issueStatus = IssueStatus.valueOf(dto.status().toUpperCase());
            issue.updateStatus(issueStatus);
        } catch (IllegalArgumentException e) {
            throw new IssueCustomException(IssueErrorCode.INVALID_ISSUE_TYPE);
        }

        issueRepository.save(issue);
        return IssueResponseDto.fromEntity(issue);
    }

    public IssueResponseDto deleteIssue(Long issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("Issue not found with id: " + issueId));

        issueRepository.delete(issue);

        return IssueResponseDto.fromEntity(issue);
    }

}
