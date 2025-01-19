package com.maptracker.issuemap.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.maptracker.issuemap.common.error.UserErrorCode;
import com.maptracker.issuemap.domain.user.dto.UserRequest;
import com.maptracker.issuemap.domain.user.dto.UserResponse;
import com.maptracker.issuemap.domain.user.dto.UserResponseDto;
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
    void 회원가입_성공시_응답값이_정상적으로_반환된다() {
        // given
        UserRequest.Signup request = new UserRequest.Signup(
                "test@example.com",
                "securePassword",
                "testUser",
                "tester"
        );

        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(request.password())).thenReturn(encodedPassword);

        User savedUser = User.builder()
                .id(1L)
                .username("testUser")
                .email("test@example.com")
                .nickname("tester")
                .password(encodedPassword)
                .role(Role.USER)
                .teams(new ArrayList<>())
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // when
        UserResponseDto response = userService.registerUser(request);

        // then
        assertThat(response).isNotNull();
        // 응답 타입 확인 및 필드 검증
        if (response instanceof UserResponse.Signup signupResponse) {
            assertThat(signupResponse.username()).isEqualTo("testUser");
            assertThat(signupResponse.email()).isEqualTo("test@example.com");
        } else {
            Assertions.fail("Response is not an instance of UserResponse.Signup");
        }
    }

    @Test
    public void 이메일이_중복되면_회원가입은_실패한다() {
        // given
        UserRequest.Signup request = new UserRequest.Signup(
                "test@example.com",
                "passwd",
                "test",
                "tester"
        );
        User existingUser = createSavedUser();
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(existingUser));

        // when & then
        Assertions.assertThatThrownBy(() -> userService.registerUser(request))
                .isInstanceOf(UserException.class)
                .hasMessage(UserErrorCode.USER_ALREADY_EXIST.getDescription());
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

    @Test
    @DisplayName("회원 조회 성공 시 응답값이 정상적으로 반환된다")
    void 회원_조회_성공_테스트() {
        // given
        Long userId = 1L;
        User user = createSavedUser();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        UserResponseDto response = userService.getUserInfoById(userId);

        // then
        assertThat(response).isNotNull();
        if (response instanceof UserResponse.Info infoResponse) {
            assertThat(infoResponse.userId()).isEqualTo(userId);
            assertThat(infoResponse.email()).isEqualTo("test@example.com");
        } else {
            Assertions.fail("Response is not an instance of UserResponse.Info");
        }
    }

    @Test
    @DisplayName("회원 조회 실패 시 예외가 발생한다")
    void 회원_조회_실패_테스트() {
        // given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> userService.getUserInfoById(userId))
                .isInstanceOf(UserException.class)
                .hasMessage(UserErrorCode.USER_NOT_FOUND.getDescription());
    }

    @Test
    @DisplayName("회원 수정 성공 시 수정된 정보를 반환한다")
    void 회원_수정_성공_테스트() {
        // given
        Long userId = 1L;
        User user = createSavedUser();
        UserRequest.Update request = new UserRequest.Update(
                "updatedPassword",
                "updatedNickname"
        );

        String encodedPassword = "encodedUpdatedPassword";
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(request.password())).thenReturn(encodedPassword);

        // when
        UserResponseDto response = userService.updateUserInfo(userId, request);

        // then
        assertThat(response).isNotNull();
        if (response instanceof UserResponse.Info infoResponse) {
            assertThat(infoResponse.userId()).isEqualTo(userId);
            assertThat(infoResponse.email()).isEqualTo("test@example.com");
        } else {
            Assertions.fail("Response is not an instance of UserResponse.Info");
        }
        assertThat(user.getNickname()).isEqualTo("updatedNickname");
        assertThat(user.getPassword()).isEqualTo(encodedPassword);
    }

    @Test
    @DisplayName("회원 수정 실패 시 예외가 발생한다")
    void 회원_수정_실패_테스트() {
        // given
        Long userId = 1L;
        UserRequest.Update request = new UserRequest.Update(
                "updatedNickname",
                "updatedPassword"
        );
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> userService.updateUserInfo(userId, request))
                .isInstanceOf(UserException.class)
                .hasMessage(UserErrorCode.USER_NOT_FOUND.getDescription());
    }

    @Test
    @DisplayName("회원 삭제 성공 시 삭제가 정상적으로 이루어진다")
    void 회원_삭제_성공_테스트() {
        // given
        Long userId = 1L;
        User user = createSavedUser();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        userService.deleteUser(userId);

        // then
        verify(userRepository).delete(user);
    }
}