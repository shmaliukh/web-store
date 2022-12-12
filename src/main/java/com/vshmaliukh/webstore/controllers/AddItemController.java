package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.repositories.ItemRepositoryProvider;
import com.vshmaliukh.webstore.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.*;

@Controller
@RequestMapping("/" + ADD_ITEM_PAGE)
@AllArgsConstructor
public class AddItemController {

    final UserService userService;
    final ItemRepositoryProvider itemRepositoryProvider;

    @GetMapping
    public ModelAndView doGet(@CookieValue(defaultValue = "0") Long userId,
                              ModelMap modelMap) {
        boolean isAdminUser = userService.isAdminUser(userId);
        if (
                ! // TODO remove '!'
                isAdminUser) {
            return new ModelAndView(ADD_ITEM_PAGE, modelMap);
        }
        return new ModelAndView("redirect:/" + HOME_PAGE, modelMap);
    }

}
