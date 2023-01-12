package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.model.UnauthorizedUser;
import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.services.UnauthorizedUserService;
import com.vshmaliukh.webstore.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.USER_HOME;

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
        if(authorization){
            return new ModelAndView("unauthorizedUserPage"); // todo implement unauthorized user page -> "you should authorize to get this page"
        }
        User user = userService.getUserById(userId);
        modelMap.addAttribute("user", user);

//        User testUser = new User(); // for tests
//        testUser.setEmail("user@gmail.com");
//        testUser.setUsername("Your First User");
//        modelMap.addAttribute("user", testUser);
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

        User user = userService.getUserById(userId);
        user.setEmail(email);
        userService.save(user);
        return "redirect:/user-home";
    }

    @PostMapping("/edit-username-user-page")
    public String editUsernameUsersPage(@CookieValue Long userId, @RequestParam String username) {

        User user = userService.getUserById(userId);
        user.setUsername(username);
        userService.save(user);

        return "redirect:/user-home";
    }

}
