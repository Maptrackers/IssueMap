package com.maptracker.issuemap.domain.issue.repository;

import com.maptracker.issuemap.domain.issue.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByProjectId(Long projectId);

}
