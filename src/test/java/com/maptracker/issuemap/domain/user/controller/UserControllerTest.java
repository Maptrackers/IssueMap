package com.maptracker.issuemap.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maptracker.issuemap.domain.user.dto.UserSignupRequest;
import com.maptracker.issuemap.domain.user.dto.UserSignupResponse;
import com.maptracker.issuemap.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("회원가입 성공 테스트")
    @WithMockUser
    void signup_success() throws Exception {
        // given
        UserSignupRequest request = UserSignupRequest.builder()
                .username("testUser")
                .email("test@example.com")
                .nickname("tester")
                .password("securePassword")
                .build();

        UserSignupResponse response = UserSignupResponse.builder()
                .username("testUser")
                .email("test@example.com")
                .build();

        when(userService.signup(any(UserSignupRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복 테스트")
    @WithMockUser
    void 회원가입_실패_이메일_중복() throws Exception {
        // given
        UserSignupRequest request = UserSignupRequest.builder()
                .username("testUser")
                .email("duplicate@example.com")
                .nickname("tester")
                .password("securePassword")
                .build();

        when(userService.signup(any(UserSignupRequest.class)))
                .thenThrow(new IllegalArgumentException("이미 사용 중인 이메일입니다."));

        // when & then
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
