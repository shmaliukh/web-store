package com.vshmaliukh.webstore.controllers.handlers;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

@Component
public class CookieHandler {

    private static final int ONE_DAY = 24 * 60 * 60;

    public Cookie createCookie(Long value, String name){
        Cookie cookie = new Cookie(name,value.toString());
        cookie.setMaxAge(ONE_DAY);
        cookie.setSecure(true);
        cookie.setPath("/");
//        cookie.setHttpOnly(true);
        return cookie;
    }

}
