package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.login.LogInProvider;
import com.vshmaliukh.webstore.model.Role;
import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.vshmaliukh.webstore.login.LogInProvider.LOCAL;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements EntityValidator<User> {

    public static final String DEFAULT_PASSWORD = "1234";

    @Getter
    final UserRepository userRepository;

    public Optional<User> readUserById(Long userId) {
        if(userId != null && userId > 0){
            return userRepository.findById(userId);
        }
        log.error("problem to read user by id"
                + (userId == null ? " // id is NULL" : " // id < 1"));
        return Optional.empty();
    }

    public List<User> readAllUserList() {
        return Collections.unmodifiableList(userRepository.findAll());
    }

    // TODO add test for 'processOAuthPostLogin' method
    public void processOAuthPostLogin(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username);
        if (!isValidEntity(user) ) {
            insertUser(username, LogInProvider.GOOGLE);
        }
    }

    public void insertUser(String username, LogInProvider logInProvider) {
        User user = new User();
        user.setUsername(username);
        user.setLogInProvider(logInProvider);
        user.setEnabled(true);
        user.setRoles(Collections.emptyList());

        User savedUser = userRepository.save(user);
        log.info("Created new user: '{}', provider: '{}'", savedUser, logInProvider);
    }

    // TODO implement service layer validation
    public User createBaseUser(
                                //  @Size(min = 3, max = 50)
                                String userName,
                                //  @Email
                                String email,
                                //  @NotEmpty
                                Role role,
                                boolean enabled) {
        User user = new User();
        user.setUsername(userName);
        user.setEmail(email);
        Collection<Role> userRoles = user.getRoles() != null ? user.getRoles() : new ArrayList<>();
        userRoles.add(role);
        user.setRoles(userRoles);
        user.setLogInProvider(LOCAL);
        user.setEnabled(enabled);
        user.setPassword(DEFAULT_PASSWORD);
        return user;
    }

    public void save(User user) {
        if (isValidEntity(user)) {
            userRepository.save(user);
            log.info("saved user: {}", user);
        } else {
            log.error("user not saved // invalid user");
        }
    }

    public boolean isUserSaved(User user) {
        if (isValidEntity(user)) {
            return user.getId() != null
                    && userRepository.existsById(user.getId());
        }
        log.warn("problem to check if user saved // invalid user");
        return false;

    }

    @Override
    public boolean isValidEntity(User user) {
        boolean isValid = user != null
                && (user.getId() == null || user.getId() > 0);
        if (!isValid) {
            log.error("invalid user"
                    + (user != null
                    ? (user.getId() != null ? " // user id < 1" : "")
                    : " // user is NULL"));
        }
        return isValid;
    }

}
