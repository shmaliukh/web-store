package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.login.LogInProvider;
import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.vshmaliukh.webstore.login.LogInProvider.LOCAL;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    final UserRepository userRepository;

    public Optional<User> readUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public List<User> readAllUserList() {
        return userRepository.findAll();
    }

    public boolean isAdminUser(Long userId) {
        User user = null;
        String userRole = null;
        if (userId != null) {
            user = userRepository.getUserById(userId);
            if (user != null) {
                userRole = user.getRole();
                return userRole.equals("admin");
            }
        }
        log.warn("problem to check user 'role' // userId: '{}' // user: '{}'", userId, userRole);
        return false;
    }

    public void processOAuthPostLogin(String username) {
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            insertUser(username, LogInProvider.GOOGLE);
        }
    }

    public void insertUser(String username, LogInProvider logInProvider) {
        User user = new User();
        user.setId(1L);
        user.setUsername(username);
        user.setLogInProvider(logInProvider);
        user.setEnabled(true);

        userRepository.save(user);
        log.info("Created new user: '{}', provider: '{}'", user, logInProvider);
    }

    public Long readUserIdByName(String userName) {
        User user = userRepository.getUserByUsername(userName);
        if (user != null) {
            return user.getId();
        }
        log.warn("problem to find user entity with '{}' username // return NULL", userName);
        return null;
    }

    public User createBaseUser(String userName, String email, String role, boolean enabled) {
        User user = new User();
        user.setUsername(userName);
        user.setEmail(email);
        user.setRole(role);
        user.setLogInProvider(LOCAL);
        user.setEnabled(enabled);
        user.setPassword("1234");
        return user;
    }

    public void save(User user) {
        if (user != null) {
            userRepository.save(user);
        } else {
            log.warn("user not saved // user == NULL");
        }
    }

    public boolean isUserSaved(User user) {
        return userRepository.existsById(user.getId());
    }

    public Page<User> getPageWithUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Page<User> getPageWithUsersByUsername(String keyword, Pageable pageable) {
        return userRepository.findByUsernameIgnoreCase(keyword, pageable);
    }

}
