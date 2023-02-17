package com.vshmaliukh.webstore.controllers.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Component
@PropertySource("classpath:application.yaml")
public class CookieExpiryRefresher implements HandlerInterceptor {

    private int rememberMeTime;
    private String rememberMeCookieName;

    @Autowired
    public CookieExpiryRefresher(@Value("${app.rememberMe.time:1209600}") int rememberMeTime,
                                 // default rememberMeTime is two weeks (1209600 seconds)
                                 @Value("${app.rememberMe.cookieName:rememberMe}") String rememberMeCookieName) {
        this.rememberMeTime = rememberMeTime;
        this.rememberMeCookieName = rememberMeCookieName;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {
        refreshRememberMeCookie(request, response);
    }

    private void refreshRememberMeCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        boolean cookiesContainsRememberMe = Arrays.stream(cookies)
                .anyMatch(cookie -> cookie.getName().contentEquals(rememberMeCookieName));
        for (Cookie cookie : cookies) {
            if (cookie.getName().contentEquals(rememberMeCookieName)
                    || (cookiesContainsRememberMe && cookie.getName().contentEquals("JSESSIONID"))) {
                refreshCookie(rememberMeTime, response, cookie);
            }
        }
    }

    private void refreshCookie(int timeInSeconds, HttpServletResponse response, Cookie cookie) {
        cookie.setMaxAge(timeInSeconds);
        response.addCookie(cookie);
    }

}