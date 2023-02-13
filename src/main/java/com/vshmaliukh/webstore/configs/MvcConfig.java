package com.vshmaliukh.webstore.configs;

import com.vshmaliukh.webstore.controllers.interceptors.ValidationErrorMessageInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@EnableWebMvc
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Value("${app.validationEnabled:true}")
    public boolean validationEnabled;

    @Override
    public void addInterceptors(final InterceptorRegistry interceptorRegistry) {
        if (validationEnabled) {
            String adminPathStr = "/admin/**";
            interceptorRegistry.addInterceptor(new ValidationErrorMessageInterceptor()).addPathPatterns(adminPathStr);
            log.info("validation interceptor for '{}' path is active", adminPathStr);
        }
        // TODO implement authorisation interceptor
    }

}
