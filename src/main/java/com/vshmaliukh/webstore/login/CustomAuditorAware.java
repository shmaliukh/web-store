package com.vshmaliukh.webstore.login;

import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class CustomAuditorAware implements AuditorAware<User> {

    private final UserRepository userRepository;

    public CustomAuditorAware(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getCurrentAuditor() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext != null) {
            Authentication authentication = securityContext.getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) principal;
                String username = user.getUsername();
                return Optional.ofNullable(userRepository.findUserByUsername(username));
            } else {
                log.error("problem to register author of changes // unauthenticated user");
            }
        }
        log.warn("problem to register author of changes // securityContext is NULL");
        return Optional.empty();
    }

}