package com.vshmaliukh.webstore.config;

import com.vshmaliukh.webstore.login.*;
import com.vshmaliukh.webstore.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

import java.io.IOException;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.HOME_PAGE;
import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.OAUTH_LOGIN_PAGE;

@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    // default rememberMeTime is two weeks (1209600 seconds)
    @Value("${app.rememberMe.time:1209600}")
    private int rememberMeTime;

    @Value("${app.rememberMe.cookieName:rememberMe}")
    private String rememberMeCookieName;

    @Value("${app.rememberMe.key:uniqueAndSecret}")
    private String rememberMeKey;

    @Value("${app.webSecurity.enabled:true}")
    public boolean webSecurityEnabled;

    @Value("${app.webSecurity.login.viaGoogle:false}")
    public boolean isEnabledLoginViaGoogle;

    @Value("${spring.h2.console.path:/h2-console}")
    public String springH2ConsolePath;

    // TODO refactor
    public static final String LOG_IN_SUCCESS_URL_STR = "/" + HOME_PAGE;

    private final UserService userService;
    private final CustomOAuth2UserService oauthUserService;
    private final CustomUserDetailsService userDetailsService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    public WebSecurityConfig(UserService userService,
                             CustomOAuth2UserService oauthUserService,
                             CustomUserDetailsService userDetailsService,
                             CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.userService = userService;
        this.oauthUserService = oauthUserService;
        this.userDetailsService = userDetailsService;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService;
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
        if (webSecurityEnabled) {
            configWithSecurity(http);
        } else {
            configWithoutSecurity(http);
        }
        return http.build();
    }

    private void configWithSecurity(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http // allowed without authentication
                .authorizeRequests()
                .antMatchers(springH2ConsolePath + "/**").permitAll()
//                .antMatchers("/access-denied").permitAll()
                .antMatchers("/oauth/token").permitAll()
                .antMatchers("/", "/" + OAUTH_LOGIN_PAGE, "/oauth/**").permitAll();
        http // login config
                .formLogin()
                .defaultSuccessUrl("/admin")
                .permitAll();
        if (isEnabledLoginViaGoogle) {
            http // oauth2Login config
                    .oauth2Login().permitAll()
                    .successHandler(getAuthenticationSuccessHandler())
                    //.loginPage("/" + PAGE_LOGIN)
                    .userInfoEndpoint()
                    .userService(oauthUserService);
        }
        http // logout config
                .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll();
        http // exception handling config
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler());
        http // 'rememberMe' config
                .rememberMe()
                .rememberMeServices(rememberMeServices())
                .key(rememberMeKey)
                .tokenValiditySeconds(rememberMeTime)
                .rememberMeParameter(rememberMeCookieName);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return customAccessDeniedHandler;
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        return new CustomRememberMeServices(rememberMeKey, userDetailsService, new InMemoryTokenRepositoryImpl());
    }


    private static void configWithoutSecurity(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/**").permitAll()
                .and()
                .authorizeRequests().antMatchers("/h2-console/**").permitAll()

        // TODO set allowed paths to visit without security
        ;
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    private AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
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