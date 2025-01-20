package com.maptracker.issuemap.domain.user.entity;

import com.maptracker.issuemap.domain.team.entity.UserTeam;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "Users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTeam> teams = new ArrayList<>();


    public static User create(String email, String username, String nickname, String password) {
        return User.builder()
                .id(null)
                .username(username)
                .email(email)
                .nickname(nickname)
                .password(password)
                .role(Role.USER)
                .teams(new ArrayList<>())
                .build();
    }

    public void editMemberInformation(String nickname, String password) {
        if (nickname != null && !nickname.isEmpty()) {
            this.nickname = nickname;
        }

        if (password != null && !password.isEmpty()) {
            this.password = password;
        }
    }

}
