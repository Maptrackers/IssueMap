package com.maptracker.issuemap.domain.user.service;

import com.maptracker.issuemap.common.error.UserErrorCode;
import com.maptracker.issuemap.domain.user.dto.UserRequest;
import com.maptracker.issuemap.domain.user.dto.UserResponse;
import com.maptracker.issuemap.domain.user.dto.UserResponse.Info;
import com.maptracker.issuemap.domain.user.dto.UserResponseDto;
import com.maptracker.issuemap.domain.user.entity.User;
import com.maptracker.issuemap.domain.user.exception.UserException;
import com.maptracker.issuemap.domain.user.repository.UserRepository;
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

    public UserResponseDto registerUser(UserRequest.Signup request) {
        checkDuplicateEmail(request.email());

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = User.create(
                request.email(),
                request.username(),
                request.nickname(),
                encodedPassword);
        userRepository.save(user);

        return new UserResponse.Signup(user.getUsername(), user.getEmail());
    }

    public UserResponseDto getUserInfoById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        return new UserResponse.Info(user.getId(), user.getEmail());
    }

    public UserResponseDto updateUserInfo(Long userId, UserRequest.Update request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        String encodedPassword = passwordEncoder.encode(request.password());
        user.editMemberInformation(request.nickname(), encodedPassword);

        return new UserResponse.Info(user.getId(), user.getEmail());
    }

    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .ifPresent(userRepository::delete);
    }

    private void checkDuplicateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserException(UserErrorCode.USER_ALREADY_EXIST);
        }
    }
}
