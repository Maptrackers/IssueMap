package com.maptracker.issuemap.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserSignupResponse {
    private String username;
    private String email;
}
