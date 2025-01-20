package com.maptracker.issuemap.domain.user.service;

import com.maptracker.issuemap.common.error.UserErrorCode;
import com.maptracker.issuemap.common.global.CookieUtil;
import com.maptracker.issuemap.common.jwt.CustomUserDetails;
import com.maptracker.issuemap.domain.user.exception.UserException;
import com.maptracker.issuemap.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthService implements UserDetailsService {

    private final UserRepository userRepository;

    public void logout(HttpServletResponse response) {
        CookieUtil.clearAuthCookies(response, CookieUtil.ACCESS_TOKEN_COOKIE_NAME);
        CookieUtil.clearAuthCookies(response, CookieUtil.REFRESH_TOKEN_COOKIE_NAME);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }


}
