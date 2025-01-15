package com.maptracker.issuemap.domain.issue.repository;

import com.maptracker.issuemap.domain.issue.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {
}
