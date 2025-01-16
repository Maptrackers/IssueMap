package com.maptracker.issuemap.domain.user.mapper;

import com.maptracker.issuemap.domain.user.dto.UserSignupRequest;
import com.maptracker.issuemap.domain.user.dto.UserSignupResponse;
import com.maptracker.issuemap.domain.user.entity.Role;
import com.maptracker.issuemap.domain.user.entity.User;
import java.util.ArrayList;

public class UserMapper {

    public static User toEntity(UserSignupRequest request, String encodedPassword) {
        return User.builder()
                .id(null)
                .username(request.getUsername())
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(encodedPassword)
                .role(Role.USER)
                .teams(new ArrayList<>())
                .build();
    }

    public static UserSignupResponse toResponse(User user) {
        return UserSignupResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
