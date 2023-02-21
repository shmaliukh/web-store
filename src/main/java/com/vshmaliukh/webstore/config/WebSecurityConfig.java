package com.vshmaliukh.webstore.config;

import com.vshmaliukh.webstore.login.CustomAccessDeniedHandler;
import com.vshmaliukh.webstore.login.CustomPersistentTokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.OAUTH_LOGIN_PAGE;

@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Value("${app.rememberMe.time:1209600}") // default rememberMeTime is two weeks (1209600 seconds)
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

    @Value("${server.servlet.session.cookie.name:JSESSIONID}")
    public String sessionCookieName;

    private final UserDetailsService userDetailsService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomPersistentTokenHandler customPersistentTokenHandler;

    public WebSecurityConfig(UserDetailsService userDetailsService,
                             CustomAccessDeniedHandler customAccessDeniedHandler,
                             CustomPersistentTokenHandler customPersistentTokenHandler) {
        this.userDetailsService = userDetailsService;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.customPersistentTokenHandler = customPersistentTokenHandler;
    }

    private void configWithSecurity(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http // allowed without authentication
                .authorizeRequests()
                .antMatchers(springH2ConsolePath + "/**").permitAll()
                .antMatchers("/oauth/token").permitAll()
                .antMatchers("/", "/" + OAUTH_LOGIN_PAGE, "/oauth/**").permitAll();
        http // login config
                .formLogin()
                .defaultSuccessUrl("/admin")
                .permitAll();
        //TODO fix login via google
        //if (isEnabledLoginViaGoogle) {
        //    http // oauth2Login config
        //            .oauth2Login().permitAll()
        //            .successHandler(getAuthenticationSuccessHandler())
        //            //.loginPage("/" + PAGE_LOGIN)
        //            .userInfoEndpoint()
        //            .userService(oauthUserService);
        //}
        http // 'rememberMe' config
                .rememberMe()
                .rememberMeServices(rememberMeServices())
        ;
        http // logout config
                .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies(
                        sessionCookieName,
                        rememberMeCookieName
                ).permitAll();
        http // exception handling config
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler());
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        return customPersistentTokenHandler;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
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

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return customAccessDeniedHandler;
    }

    @Bean
    public PersistentTokenBasedRememberMeServices rememberMeServices() {
        PersistentTokenBasedRememberMeServices rememberMeServices
                = new PersistentTokenBasedRememberMeServices(rememberMeKey, userDetailsService, persistentTokenRepository());
        rememberMeServices.setCookieName(rememberMeCookieName);
        rememberMeServices.setTokenValiditySeconds(rememberMeTime);
        rememberMeServices.setAlwaysRemember(true);
        rememberMeServices.setUseSecureCookie(true);
        return rememberMeServices;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "" +
                "ROLE_DEV > ROLE_ADMIN \n" +
                "ROLE_ADMIN > ROLE_STAFF \n" +
                "ROLE_STAFF > ROLE_USER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    private static void configWithoutSecurity(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/**").permitAll();
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

}