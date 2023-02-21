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

}
