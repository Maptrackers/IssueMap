package com.maptracker.issuemap.domain.user.service;

import com.maptracker.issuemap.common.error.UserErrorCode;
import com.maptracker.issuemap.domain.user.dto.UserResponse;
import com.maptracker.issuemap.domain.user.dto.UserSignupRequest;
import com.maptracker.issuemap.domain.user.dto.UserSignupResponse;
import com.maptracker.issuemap.domain.user.entity.User;
import com.maptracker.issuemap.domain.user.exception.UserException;
import com.maptracker.issuemap.domain.user.mapper.UserMapper;
import com.maptracker.issuemap.domain.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
        User userEntity = UserMapper.toEntity(request, encodedPassword);
        User user = userRepository.save(userEntity);

        return UserMapper.toResponse(user);
    }


    public UserResponse findByUserId(Long userId) {
        return userRepository.findById(userId)
                .map(user -> new UserResponse(user.getId(), user.getEmail()))
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    private void validateDuplicateEmail(String email) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new UserException(UserErrorCode.USER_ALREADY_EXIST);
                });
    }

}
