package com.maptracker.issuemap.domain.issue.repository;

import com.maptracker.issuemap.domain.issue.entity.SubIssue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubIssueRepository extends JpaRepository<SubIssue, Long> {
}
