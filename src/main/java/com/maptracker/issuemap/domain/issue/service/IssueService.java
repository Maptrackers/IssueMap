package com.maptracker.issuemap.domain.issue.service;

import com.maptracker.issuemap.domain.issue.dto.issue.IssueCreateRequestDto;
import com.maptracker.issuemap.domain.issue.entity.Issue;
import com.maptracker.issuemap.domain.issue.repository.IssueRepository;
import com.maptracker.issuemap.domain.project.entity.Project;
import com.maptracker.issuemap.domain.project.repository.ProjectRepository;
import com.maptracker.issuemap.domain.user.entity.User;
import com.maptracker.issuemap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        Issue issue = Issue.create(user, project, dto.title(), dto.issueType());
        issueRepository.save(issue);

        return dto;
    }

}
