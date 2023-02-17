package com.vshmaliukh.webstore.config;

import com.vshmaliukh.webstore.controllers.interceptors.CookieExpiryRefresher;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final CookieExpiryRefresher cookieExpiryRefresher;

    public WebMvcConfig(CookieExpiryRefresher cookieExpiryRefresher) {
        this.cookieExpiryRefresher = cookieExpiryRefresher;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(cookieExpiryRefresher);
    }

}
