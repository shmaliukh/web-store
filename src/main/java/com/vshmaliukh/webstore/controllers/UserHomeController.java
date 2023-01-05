package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.USER_HOME;
import static com.vshmaliukh.webstore.controllers.ViewsNames.HOME_PAGE_VIEW;

@Controller
@RequestMapping("/" + USER_HOME)
@AllArgsConstructor
public class UserHomeController {

    @GetMapping
    public String showHomePage(ModelMap modelMap,
                               @RequestHeader String referer) {
        User user = new User();
        user.setEmail("user@gmail.com");
        user.setUsername("Your First User");
        modelMap.addAttribute("user", user);
        return HOME_PAGE_VIEW;
    }

    @GetMapping("/{data}")
    public ModelAndView showUsersEditPage(@PathVariable String data, ModelMap modelMap) {
        modelMap.addAttribute("form", data);
        return new ModelAndView("edit-data-users-page", modelMap);
    }

}
