package com.maptracker.issuemap.domain.user.service;

import com.maptracker.issuemap.domain.user.dto.UserSignupRequest;
import com.maptracker.issuemap.domain.user.dto.UserSignupResponse;
import com.maptracker.issuemap.domain.user.entity.Role;
import com.maptracker.issuemap.domain.user.entity.User;
import com.maptracker.issuemap.domain.user.repository.UserRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSignupResponse signup(UserSignupRequest request) {
        validateDuplicateEmail(request.getEmail());

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User userDTO = new User().builder()
                .id(null)
                .username(request.getUsername())
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(encodedPassword)
                .role(Role.USER)
                .teams(new ArrayList<>())
                .build();

        User user = userRepository.save(userDTO);

        return UserSignupResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    private void validateDuplicateEmail(String email) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
                });
    }

}
