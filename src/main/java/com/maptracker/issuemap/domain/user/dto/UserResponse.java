package com.maptracker.issuemap.domain.user.dto;

public abstract class UserResponse {
    public record Info(Long userId, String email) implements UserResponseDto {
    }

    public record Login(Long userId, String email, String token) implements UserResponseDto {
    }

    public record Signup(String username, String email) implements UserResponseDto {
    }
}
