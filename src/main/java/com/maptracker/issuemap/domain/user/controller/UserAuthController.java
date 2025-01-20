package com.maptracker.issuemap.domain.user.controller;

import com.maptracker.issuemap.domain.user.dto.UserRequest;
import com.maptracker.issuemap.domain.user.service.UserAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserAuthController {

    private final UserAuthService userAuthService;


    @Operation(
            summary = "사용자 로그인 API",
            description = "사용자 로그인 요청을 처리합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody UserRequest.Login request) {
        return ResponseEntity.ok().build();
    }


    @Operation(
            summary = "사용자 로그아웃 API",
            description = "사용자 로그아웃 요청을 처리하고 인증 쿠키를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        userAuthService.logout(request, response);
        return ResponseEntity.ok().build();

    }
}
