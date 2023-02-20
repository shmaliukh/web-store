package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.services.UnauthorizedUserService;
import com.vshmaliukh.webstore.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/user-home")
public class UserHomeController {

    UserService userService;
    UnauthorizedUserService unauthorizedUserService;

    @GetMapping
    public ModelAndView showHomePage(ModelMap modelMap,
                                     @CookieValue(defaultValue = "0") Long userId) {

        // todo implement authorizing checking

        boolean authorization = false;
        if(!authorization){
            return new ModelAndView("unauthorized-user-page");
        }
        Optional<User> optionalUser = userService.readUserById(userId);
        optionalUser.ifPresent(user -> modelMap.addAttribute("user", user));
        return new ModelAndView("/user/user-home-page");
    }

    @GetMapping("/edit")
    public ModelAndView showUsersEditPage(@CookieValue Long userId) {
        // todo implement authorizing checking
        boolean authorization = false;
        if(authorization){
            return new ModelAndView("/user/unauthorized-user-page");
        }
        return new ModelAndView("/user/edit-data-users-page");
    }

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
