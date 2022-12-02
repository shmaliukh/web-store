package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.login.LogInProvider;
import com.vshmaliukh.webstore.entities.UserEntity;
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
        UserEntity user = userRepository.getUserEntityByUsername(username);
        if (user == null) {
            user = new UserEntity();
            user.setUsername(username);
            user.setLogInProvider(LogInProvider.GOOGLE);

            userRepository.save(user);
            log.info("Created new user: '{}'", user);
        }
    }

}
