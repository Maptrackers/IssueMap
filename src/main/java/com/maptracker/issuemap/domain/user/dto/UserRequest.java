package com.maptracker.issuemap.domain.user.dto;

public abstract class UserRequest {

    public record Signup(String email, String password, String username, String nickname) {
    }

    public record Login(String email, String password) {
    }

    public record Update(String password, String nickname) {
    }

}
