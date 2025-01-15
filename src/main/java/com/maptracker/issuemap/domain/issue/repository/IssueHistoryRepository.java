package com.maptracker.issuemap.domain.issue.repository;

import com.maptracker.issuemap.domain.issue.entity.IssueHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueHistoryRepository extends JpaRepository<IssueHistory, Long> {
}
