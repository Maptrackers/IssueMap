package com.maptracker.issuemap.domain.issue.repository;

import com.maptracker.issuemap.domain.issue.entity.SubIssue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubIssueRepository extends JpaRepository<SubIssue, Long> {

    List<SubIssue> findByIssueId(Long issueId);

}
