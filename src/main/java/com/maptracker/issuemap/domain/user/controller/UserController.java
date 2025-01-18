package com.maptracker.issuemap.domain.user.controller;

import com.maptracker.issuemap.common.jwt.CustomUserDetails;
import com.maptracker.issuemap.domain.user.dto.UserRequest;
import com.maptracker.issuemap.domain.user.dto.UserResponseDto;
import com.maptracker.issuemap.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "사용자 등록 API",
            description = "사용자를 등록하고, 등록된 사용자의 정보를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용자 등록 성공"),
                    @ApiResponse(responseCode = "400", description = "사용자 등록 실패")
            }
    )
    @PostMapping
    public ResponseEntity<UserResponseDto> signup(@RequestBody UserRequest.Signup request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @Operation(
            summary = "사용자 정보 조회 API",
            description = "현재 인증된 사용자의 정보를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
            }
    )
    @GetMapping
    public ResponseEntity<UserResponseDto> getUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(userService.getUserInfoById(userDetails.getUserId()));
    }


}
