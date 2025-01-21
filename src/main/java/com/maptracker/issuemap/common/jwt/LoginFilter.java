package com.maptracker.issuemap.common.jwt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maptracker.issuemap.common.global.CookieUtil;
import com.maptracker.issuemap.domain.user.dto.UserResponse.Login;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        String email;
        String password;

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            try (BufferedReader reader = request.getReader()) {
                Map<String, String> credentials = objectMapper.readValue(reader,
                        new TypeReference<Map<String, String>>() {
                        });

                email = credentials.get("email");
                password = credentials.get("password");
            }
        } catch (Exception e) {
            throw new RuntimeException("로그인 요청 파싱 오류");
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
        return authenticationManager.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        Long userId = customUserDetails.getUserId();
        String userEmail = customUserDetails.getUsername();

        String accessToken = jwtUtil.createJwt(userId, userEmail, 60 * 30L);
        String refreshToken = jwtUtil.createJwt(userId, userEmail, 60 * 60 * 24L * 7L);
        CookieUtil.createCookies(response, "refreshToken", refreshToken, 60 * 60 * 24 * 7);

        Login login = new Login(userId, userEmail, accessToken);
        ResponseEntity<Login> loginUserApiResponse = ResponseEntity.ok(login);

        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json");
        objectMapper.writeValue(response.getOutputStream(), loginUserApiResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("message", "ID 또는 PW가 일치하지 않습니다.");
        errorResponse.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
