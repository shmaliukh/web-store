package com.vshmaliukh.webstore.controllers.admin;

import com.vshmaliukh.webstore.repositories.ActionsWithItemRepositoryProvider;
import com.vshmaliukh.webstore.services.ItemService;
import com.vshmaliukh.webstore.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.*;
import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.HOME_PAGE;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminHomeController {

    final UserService userService;
    final ItemService itemService;
    final ActionsWithItemRepositoryProvider itemRepositoryProvider;

    @GetMapping
    public ModelAndView doGet(@CookieValue(defaultValue = "0") Long userId,
                              ModelMap modelMap) {
        Map<String, Integer> categoryItemQuantityMap = Collections.singletonMap("Literature", 3);
        // TODO implement items by category calc
        modelMap.addAttribute("categoryItemQuantityMap", categoryItemQuantityMap);
        boolean isAdminUser = userService.isAdminUser(userId);
        if (
                ! // TODO remove '!'
                        isAdminUser) {
            return new ModelAndView(ADMIN_HOME_PAGE, modelMap);
        }
        // TODO create interceptor for admin verification
        return new ModelAndView("redirect:/" + HOME_PAGE, modelMap);
    }

    @GetMapping("/exit")
    public ModelAndView exit(ModelMap modelMap) {
        // TODO implement exit
        return new ModelAndView("redirect:/" + HOME_PAGE, modelMap);
    }
}

