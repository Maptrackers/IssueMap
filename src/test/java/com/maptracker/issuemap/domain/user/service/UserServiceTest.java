package com.maptracker.issuemap.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.maptracker.issuemap.common.error.UserErrorCode;
import com.maptracker.issuemap.domain.user.dto.UserSignupRequest;
import com.maptracker.issuemap.domain.user.dto.UserSignupResponse;
import com.maptracker.issuemap.domain.user.entity.Role;
import com.maptracker.issuemap.domain.user.entity.User;
import com.maptracker.issuemap.domain.user.exception.UserException;
import com.maptracker.issuemap.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("회원가입 성공시 응답값이 정상적으로 반환된다")
    void 회원가입_성공시_응답값이_정상적으로_반환된다() {
        // given
        UserSignupRequest request = UserSignupRequest.builder()
                .username("testUser")
                .email("test@example.com")
                .nickname("tester")
                .password("securePassword")
                .build();

        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);

        User savedUser = User.builder()
                .id(1L)
                .username("testUser")
                .email("test@example.com")
                .nickname("tester")
                .password(encodedPassword)
                .role(Role.USER)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // when
        UserSignupResponse response = userService.signup(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo("testUser");
        assertThat(response.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void 이메일이_중복되면_회원가입은_실패한다() {
        //given
        UserSignupRequest request = createSignupRequest();
        User existingUser = createSavedUser();
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));

        //when && then
        Assertions.assertThatThrownBy(() -> userService.signup(request))
                .isInstanceOf(UserException.class)
                .hasMessage(UserErrorCode.USER_ALREADY_EXIST.getDescription());
    }

    private static UserSignupRequest createSignupRequest() {
        return UserSignupRequest.builder()
                .username("test")
                .email("test@example.com")
                .nickname("tester")
                .password("passwd")
                .build();
    }

    private User createSavedUser() {
        return User.builder()
                .id(1L)
                .username("testUser")
                .email("test@example.com")
                .nickname("testNickname")
                .password("securePassword")
                .role(Role.USER)
                .teams(new ArrayList<>())
                .build();
    }

}