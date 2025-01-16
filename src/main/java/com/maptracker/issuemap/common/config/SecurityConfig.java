package com.maptracker.issuemap.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Swagger 관련 경로 허용
                        .requestMatchers("/api/teams/**").permitAll() // 특정 API 경로 허용
                        .requestMatchers("/api/users/signup").permitAll() // 회원가입 경로 허용
                        .anyRequest().authenticated() // 그 외 요청 인증 필요
                )
                .httpBasic(httpBasic -> httpBasic.realmName("YourRealm")); // HTTP Basic 인증 활성화 및 Realm 설정

        return http.build();
    }
}
