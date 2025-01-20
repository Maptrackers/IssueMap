package com.maptracker.issuemap.common.jwt;

import com.maptracker.issuemap.domain.user.exception.UserException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;


public class GlobalExceptionHandlingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (UserException ex) {
            response.setStatus(ex.getErrorCode().getHttpStatusCode());
            response.setContentType("application/json;charset=UTF-8");
            String jsonResponse = String.format("{\"code\": \"%d\", \"message\": \"%s\"}",
                    ex.getErrorCode().getErrorCode(),
                    ex.getErrorCode().getDescription());
            response.getWriter().write(jsonResponse);
            response.getWriter().flush();
        }
    }
}
