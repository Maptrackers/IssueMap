package com.maptracker.issuemap.common.jwt;

import com.maptracker.issuemap.domain.user.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String ACCESS_TOKEN_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = extractToken(request, ACCESS_TOKEN_HEADER);
        String refreshToken = extractToken(request, REFRESH_TOKEN_HEADER);

        if (accessToken == null && refreshToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if ((accessToken == null || jwtUtil.isExpired(accessToken)) && (refreshToken == null || jwtUtil.isExpired(refreshToken))) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access and Refresh tokens are expired. Please login again.");
            return;
        }

        if ((accessToken == null || jwtUtil.isExpired(accessToken)) && refreshToken != null && !jwtUtil.isExpired(refreshToken)) {
            String userEmail = jwtUtil.getUserEmail(refreshToken);
            Long userId = jwtUtil.getUserId(refreshToken);
            String newAccessToken = jwtUtil.createJwt(userId, userEmail, 60 * 60L);
            response.setHeader(ACCESS_TOKEN_HEADER, BEARER_PREFIX + newAccessToken);
            setAuthentication(userId, userEmail);
            accessToken = newAccessToken;
        }

        if (accessToken != null && !jwtUtil.isExpired(accessToken)
                && (refreshToken == null || jwtUtil.isExpired(refreshToken))) {
            String userEmail = jwtUtil.getUserEmail(accessToken);
            Long userId = jwtUtil.getUserId(accessToken);
            String newRefreshToken = jwtUtil.createJwt(userId, userEmail, 60 * 60L * 24L * 7L);
            response.setHeader(REFRESH_TOKEN_HEADER, BEARER_PREFIX + newRefreshToken);
        }

        if (accessToken != null && !jwtUtil.isExpired(accessToken)) {
            String userEmail = jwtUtil.getUserEmail(accessToken);
            Long userId = jwtUtil.getUserId(accessToken);
            setAuthentication(userId, userEmail);
        }

        String userEmail = jwtUtil.getUserEmail(accessToken);
        Long userId = jwtUtil.getUserId(accessToken);
        User userEntity = User.builder()
                .id(userId)
                .email(userEmail)
                .password("temp")
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
                customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(Long userId, String userEmail) {
        User userEntity = User.builder()
                .id(userId)
                .email(userEmail)
                .password("temp")
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
                customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private String extractToken(HttpServletRequest request, String headerName) {
        String header = request.getHeader(headerName);
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            return header.substring(BEARER_PREFIX.length());
        }
        return null;
    }

}
