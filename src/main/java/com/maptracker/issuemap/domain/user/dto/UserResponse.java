package com.maptracker.issuemap.domain.user.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long userId;
    private String email;

    public UserResponse(Long userId, String email) {
        this.userId = userId;
        this.email = email;
    }
}
