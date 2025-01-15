package com.maptracker.issuemap.domain.user.repository;

import com.maptracker.issuemap.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
