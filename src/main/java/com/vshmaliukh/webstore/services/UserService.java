package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.login.LogInProvider;
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

    public void processOAuthPostLogin(String username) {
        User user = userRepository.getUserEntityByUsername(username);
        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setLogInProvider(LogInProvider.GOOGLE);
            user.setEnabled(true);

            userRepository.save(user);
            log.info("Created new user: '{}'", user);
        }
    }

}
