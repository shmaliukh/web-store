package com.vshmaliukh.webstore.controllers.handlers;

import javax.servlet.http.Cookie;

public class CookieHandler {

    public Cookie createUserIdCookie(Long userId){
        Cookie cookie = new Cookie("userId",userId.toString());
        cookie.setMaxAge(24*60*60);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        return cookie;
    }

}
