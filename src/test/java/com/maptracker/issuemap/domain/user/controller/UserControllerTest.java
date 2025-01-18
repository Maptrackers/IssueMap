package com.maptracker.issuemap.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maptracker.issuemap.common.error.UserErrorCode;
import com.maptracker.issuemap.domain.user.exception.UserException;
import com.maptracker.issuemap.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
    @WithMockUser
    void 회원가입_성공시_응답값이_정상적으로_반환된다() throws Exception {
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

        when(userService.registerUser(any(UserSignupRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @WithMockUser
    void 회원가입_실패_이메일_중복() throws Exception {
        // given
        UserSignupRequest request = UserSignupRequest.builder()
                .username("testUser")
                .email("duplicate@example.com")
                .nickname("tester")
                .password("securePassword")
                .build();

        when(userService.registerUser(any(UserSignupRequest.class)))
                .thenThrow(new UserException(UserErrorCode.USER_ALREADY_EXIST));

        // when & then
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
