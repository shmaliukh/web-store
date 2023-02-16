package com.vshmaliukh.webstore.login;

import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Date;

@Slf4j
public class CustomRememberMeServices extends PersistentTokenBasedRememberMeServices {

    // TODO solve 'Autowired members must be defined in valid Spring bean (@Component|@Service|...) '
    @Autowired
    private UserRepository userRepository;

    private final String key;
    private final PersistentTokenRepository tokenRepository;
    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

    public CustomRememberMeServices(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
        this.tokenRepository = tokenRepository;
        this.key = key;
    }

    @Override
    protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        Object principal = successfulAuthentication.getPrincipal();
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) principal;
        String username = user.getUsername();
        log.debug("creating new persistent login for user " + username);
        String generateTokenData = generateTokenData();
        PersistentRememberMeToken persistentToken = new PersistentRememberMeToken(username, generateSeriesData(), generateTokenData, new Date());
        try {
            tokenRepository.createNewToken(persistentToken);
            addCookie(persistentToken, request, response);
        } catch (Exception e) {
            log.error("failed to save persistent token", e);
        }
    }

    @Override
    protected Authentication createSuccessfulAuthentication(HttpServletRequest request, UserDetails user) {
        User userFromRepository = userRepository.findUserByUsername(user.getUsername());
        Collection<? extends GrantedAuthority> grantedAuthorities = authoritiesMapper.mapAuthorities(user.getAuthorities());
        RememberMeAuthenticationToken auth = new RememberMeAuthenticationToken(key, userFromRepository, grantedAuthorities);
        auth.setDetails(authenticationDetailsSource.buildDetails(request));
        return auth;
    }

    private void addCookie(PersistentRememberMeToken token, HttpServletRequest request, HttpServletResponse response) {
        setCookie(new String[]{token.getSeries(), token.getTokenValue()}, getTokenValiditySeconds(), request, response);
    }

}
