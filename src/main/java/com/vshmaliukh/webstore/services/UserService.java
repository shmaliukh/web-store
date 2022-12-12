package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.login.LogInProvider;
import com.vshmaliukh.webstore.model.Role;
import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isAdminUser(Long userId){
        Role userRole = null;
        if(userId != null){
            User user = userRepository.getUserById(userId);
            userRole = user.getRole();
            if(userRole == null){
                String roleName = userRole.getName();
                return roleName.equals("admin");
            }
        }
        log.warn("problem to check user 'role' // userId: '{}' // userRole: '{}'", userId, userRole);
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

}
