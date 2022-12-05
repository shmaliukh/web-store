package com.vshmaliukh.webstore;

import com.vshmaliukh.webstore.login.CustomOAuth2User;
import com.vshmaliukh.webstore.login.CustomOAuth2UserService;
import com.vshmaliukh.webstore.services.UserDetailsServiceImpl;
import com.vshmaliukh.webstore.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.*;

@Slf4j
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    public static final String LOG_IN_SUCCESS_URL_STR = "/" + PAGE_HOME;
    private final UserService userService;
    private final CustomOAuth2UserService oauthUserService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public WebSecurityConfig(UserService userService,
                             CustomOAuth2UserService oauthUserService,
                             UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userService = userService;
        this.oauthUserService = oauthUserService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsServiceImpl;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/", "/" + PAGE_LOGIN, "/oauth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .loginPage("/" + PAGE_LOGIN)
//                .usernameParameter("email")
//                .passwordParameter("pass")
//                .defaultSuccessUrl(LOG_IN_SUCCESS_URL_STR)
                .and()
                .oauth2Login().permitAll()
//                .loginPage("/" + PAGE_LOGIN)
                .userInfoEndpoint()
                .userService(oauthUserService)
                .and()
                .successHandler(getAuthenticationSuccessHandler())
                .defaultSuccessUrl(LOG_IN_SUCCESS_URL_STR)
                .and()
                .logout().logoutSuccessUrl("/").permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/" + PAGE_403)
        ;
        return http.build();
    }

    private AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            log.info("AuthenticationSuccessHandler invoked");
            log.info("Authentication name: " + authentication.getName());
            CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
            userService.processOAuthPostLogin(oauthUser.getEmail());
            try {
                response.sendRedirect(LOG_IN_SUCCESS_URL_STR);
            } catch (IOException ioe) {
                log.warn("problem to redirect to '{}' page", LOG_IN_SUCCESS_URL_STR);
                log.error(ioe.getMessage(), ioe);
            }
        };
    }

}