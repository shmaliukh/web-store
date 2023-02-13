package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.services.UnauthorizedUserService;
import com.vshmaliukh.webstore.services.UserService;
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
        return new ModelAndView("user-home-page");
    }

    @GetMapping("/{data}")
    public ModelAndView showUsersEditPage(@PathVariable String data, ModelMap modelMap, @CookieValue Long userId) {

        // todo implement authorizing checking

        boolean authorization = false;
        if(authorization){
            return new ModelAndView("unauthorizedUserPage");
        }

        modelMap.addAttribute("form", data);
        return new ModelAndView("edit-data-users-page", modelMap);
    }

    @PostMapping("/edit-email-user-page")
    public String editEmailUsersPage(@CookieValue Long userId, @RequestParam String email) {


        Optional<User> optionalUser = userService.readUserById(userId);
        if(optionalUser.isPresent()){
            User user =  optionalUser.get();
            user.setEmail(email);
            userService.save(user);
        }
        return "redirect:/user-home";
    }

    @PostMapping("/edit-username-user-page")
    public String editUsernameUsersPage(@CookieValue Long userId, @RequestParam String username) {

        Optional<User> optionalUser = userService.readUserById(userId);
        if(optionalUser.isPresent()){
            User user =  optionalUser.get();
            user.setUsername(username);
            userService.save(user);
        }
        return "redirect:/user-home";
    }

}
