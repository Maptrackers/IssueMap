package com.maptracker.issuemap.common.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private Key key;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Long getUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);
    }

    public String getUserEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token).
                getBody()
                .get("email", String.class);
    }

    public Boolean isExpired(String token) {
        try {
            Date expiration = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (JwtException e) {
            throw e;
        }
    }

    public String createJwt(Long userId, String username, Long expiredMs) {
        var claims = Jwts.claims();
        claims.put("id", userId);
        claims.put("email", username);

        ZonedDateTime nowKst = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        Instant nowUtc = nowKst.toInstant();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(nowUtc))
                .setExpiration(Date.from(nowUtc.plusSeconds(expiredMs)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
