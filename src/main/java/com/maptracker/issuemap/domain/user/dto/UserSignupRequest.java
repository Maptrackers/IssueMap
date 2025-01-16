package com.maptracker.issuemap.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserSignupRequest {
    private String email;
    private String username;
    private String nickname;
    private String password;
}
