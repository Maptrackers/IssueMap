package com.maptracker.issuemap.domain.issue.service;

import com.maptracker.issuemap.domain.issue.dto.issue.*;
import com.maptracker.issuemap.domain.issue.entity.Issue;
import com.maptracker.issuemap.domain.issue.entity.IssueHistory;
import com.maptracker.issuemap.domain.issue.entity.IssueStatus;
import com.maptracker.issuemap.domain.issue.exception.IssueCustomException;
import com.maptracker.issuemap.domain.issue.exception.IssueErrorCode;
import com.maptracker.issuemap.domain.issue.repository.IssueHistoryRepository;
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
    private final IssueHistoryRepository issueHistoryRepository;
    private final ProjectRepository projectRepository;

    public IssueCreateRequestDto createIssue(IssueCreateRequestDto dto, Long userId) {

        // 나중에 에러처리 추가
        User user = userRepository.findById(userId).orElseThrow();
        Project project = projectRepository.findById(dto.projectId()).orElseThrow();

        try {
            Issue issue = Issue.create(user, project, dto.title(), dto.issueType());
            issueRepository.save(issue);

            IssueHistory issueHistory = IssueHistory.create(user, issue, "이슈 생성함", null);
            issueHistoryRepository.save(issueHistory);
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

    public IssueResponseDto updateIssueTitle(IssueUpdateTitleRequestDto dto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Issue issue = issueRepository.findById(dto.issueId())
                .orElseThrow(() -> new IssueCustomException(IssueErrorCode.ISSUE_NOT_FOUND));

        String beforeTitle = issue.getTitle();

        issue.updateTitle(dto.title());

        IssueHistory issueHistory = IssueHistory.create(user, issue, "제목 변경됨", beforeTitle + " -> "+ dto.title());

        issueHistoryRepository.save(issueHistory);
        issueRepository.save(issue);
        return IssueResponseDto.fromEntity(issue);
    }

    public IssueResponseDto updateIssueContent(IssueUpdateContentRequestDto dto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Issue issue = issueRepository.findById(dto.issueId())
                .orElseThrow(() -> new IssueCustomException(IssueErrorCode.ISSUE_NOT_FOUND));

        String beforeContent = issue.getContent();

        issue.updateContent(dto.content());

        IssueHistory issueHistory = IssueHistory.create(user, issue, "내용 업데이트됨", beforeContent + " -> "+ dto.content());

        issueHistoryRepository.save(issueHistory);
        issueRepository.save(issue);
        return IssueResponseDto.fromEntity(issue);
    }


    public IssueResponseDto updateIssueStatus(IssueUpdateStatusRequestDto dto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Issue issue = issueRepository.findById(dto.issueId())
                .orElseThrow(() -> new IssueCustomException(IssueErrorCode.ISSUE_NOT_FOUND));

        String beforeStatus = String.valueOf(issue.getIssueStatus());

        try {
            IssueStatus issueStatus = IssueStatus.valueOf(dto.status().toUpperCase());
            issue.updateStatus(issueStatus);
            IssueHistory issueHistory = IssueHistory.create(user, issue, "상태 업데이트됨", beforeStatus + " -> "+ dto.status());
            issueHistoryRepository.save(issueHistory);
        } catch (IllegalArgumentException e) {
            throw new IssueCustomException(IssueErrorCode.INVALID_ISSUE_TYPE);
        }


        issueRepository.save(issue);
        return IssueResponseDto.fromEntity(issue);
    }

    public IssueResponseDto deleteIssue(Long issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("Issue not found with id: " + issueId));

        issueHistoryRepository.deleteAllByIssueId(issueId);
        issueRepository.delete(issue);

        return IssueResponseDto.fromEntity(issue);
    }

}
