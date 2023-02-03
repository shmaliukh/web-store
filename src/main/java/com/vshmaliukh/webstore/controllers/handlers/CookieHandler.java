package com.vshmaliukh.webstore.controllers.handlers;

import javax.servlet.http.Cookie;

public class CookieHandler {

    public Cookie createCookie(Long value, String name){
        Cookie cookie = new Cookie(name,value.toString());
        cookie.setMaxAge(24*60*60*100);
        cookie.setSecure(true);
        cookie.setPath("/");
//        cookie.setHttpOnly(true);
        return cookie;
    }

}
