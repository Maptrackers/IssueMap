package com.maptracker.issuemap.common.global;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

    public static final int COOKIE_EXPIRED_AGE = 0;
    public static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    private CookieUtil(){

    }


    public static void createCookies(HttpServletResponse response, String cookieName, String data, int exp) {
        Cookie cookie = new Cookie(cookieName, data);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(exp);
        response.addCookie(cookie);
    }
    public static void clearAuthCookies(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(COOKIE_EXPIRED_AGE);
        response.addCookie(cookie);
    }
}
