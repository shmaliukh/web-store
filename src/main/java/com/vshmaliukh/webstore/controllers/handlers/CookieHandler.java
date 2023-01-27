package com.vshmaliukh.webstore.controllers.handlers;

import javax.servlet.http.Cookie;

public class CookieHandler {

    private static final int ONE_DAY = 24 * 60 * 60;

    public Cookie createUserIdCookie(Long userId) {
        Cookie cookie = new Cookie("userId", userId.toString());
        cookie.setMaxAge(ONE_DAY);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        return cookie;
    }

}
