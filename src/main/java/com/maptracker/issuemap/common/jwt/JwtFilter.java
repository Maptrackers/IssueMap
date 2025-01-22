package com.maptracker.issuemap.common.jwt;

import static com.maptracker.issuemap.common.error.UserErrorCode.USER_NOT_AUTHENTICATED;
import static org.apache.logging.log4j.ThreadContext.isEmpty;

import com.maptracker.issuemap.common.error.UserErrorCode;
import com.maptracker.issuemap.domain.user.entity.User;
import com.maptracker.issuemap.domain.user.exception.UserException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        validateTokenHeader(request, ACCESS_TOKEN_HEADER);
        String accessToken = extractToken(request, ACCESS_TOKEN_HEADER);

        if (jwtUtil.isExpired(accessToken)) {
            String refreshToken = extractToken(request, REFRESH_TOKEN_HEADER);

            if (refreshToken == null || jwtUtil.isExpired(refreshToken)) {
                throw new UserException(USER_NOT_AUTHENTICATED);
            }

            if (isTokenBlacklisted(refreshToken)) {
                throw new UserException(USER_NOT_AUTHENTICATED);
            }

            String userEmail = jwtUtil.getUserEmail(refreshToken);
            Long userId = jwtUtil.getUserId(refreshToken);
            String newAccessToken = jwtUtil.createJwt(userId, userEmail, 60 * 60 * 10L);
            response.setHeader(ACCESS_TOKEN_HEADER, BEARER_PREFIX + newAccessToken);

            setAuthentication(userId, userEmail);
            filterChain.doFilter(request, response);
            return;
        }

        setAuthentication(jwtUtil.getUserId(accessToken), jwtUtil.getUserEmail(accessToken));
        filterChain.doFilter(request, response);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();
        return uri.contains("login")
                || uri.contains("swagger-ui")
                || uri.contains("swagger-resources")
                || uri.contains("v3/api-docs")
                || uri.contains("webjars")
                || uri.contains("users") && request.getMethod().equals("POST");
    }

    private void validateTokenHeader(HttpServletRequest request, String header) {
        String tokenHeader = request.getHeader(header);
        if (tokenHeader == null || !tokenHeader.startsWith(BEARER_PREFIX)) {
            throw new UserException(UserErrorCode.USER_NOT_FOUND);
        }
    }

    private boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("user:blacklist:refreshToken:" + token));
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
