package com.maptracker.issuemap.domain.user.service;

import com.maptracker.issuemap.common.error.UserErrorCode;
import com.maptracker.issuemap.common.global.CookieUtil;
import com.maptracker.issuemap.common.jwt.CustomUserDetails;
import com.maptracker.issuemap.domain.user.exception.UserException;
import com.maptracker.issuemap.domain.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        Cookie refreshCookie = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshCookie = cookie;
                    break;
                }
            }
        }
        redisTemplate.opsForValue().set(
                "user:blacklist:refreshToken:" + refreshCookie.getValue(),
                refreshCookie.getValue(),
                60L * 60L * 24L * 7L,
                TimeUnit.SECONDS);
        CookieUtil.clearAuthCookies(response, CookieUtil.REFRESH_TOKEN_COOKIE_NAME);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }


}
