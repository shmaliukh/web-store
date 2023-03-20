package com.vshmaliukh.webstore.controllers.rest;

import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    UserService userService;

    @PutMapping("/edit-email-user-page")
    public ResponseEntity<User> editEmailUsersPage(@CookieValue Long userId, @RequestBody String email) {
        Optional<User> optionalUser = userService.readUserById(userId);
        if(optionalUser.isPresent()){
            User user =  optionalUser.get();
            user.setEmail(email);
            userService.save(user);
            return ResponseEntity.ok(user); // todo remove User usage - use DTO instead
//            new org.springframework.security.core.userdetails.User(user.getUsername(), user.getEmail());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/edit-username-user-page")
    public ResponseEntity<User> editUsernameUsersPage(@CookieValue Long userId, @RequestBody String username) {
        Optional<User> optionalUser = userService.readUserById(userId);
        if(optionalUser.isPresent()){
            User user =  optionalUser.get();
            user.setUsername(username);
            userService.save(user);
            return ResponseEntity.ok(user);  // todo remove User usage - use DTO instead
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/edit-avatar-user-page")
    public ResponseEntity<User> editAvatarUsersPage(@CookieValue Long userId, @RequestBody String avatar) { // todo implement avatar setting
        Optional<User> optionalUser = userService.readUserById(userId);
        if(optionalUser.isPresent()){
            User user =  optionalUser.get();
            userService.save(user);
            return ResponseEntity.ok(user);  // todo remove User usage - use DTO instead
        }
        return ResponseEntity.notFound().build();
    }

}
