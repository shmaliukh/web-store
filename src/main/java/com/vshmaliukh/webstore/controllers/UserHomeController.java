package com.vshmaliukh.webstore.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.EDIT_PAGE;
import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.USER_HOME;
import static com.vshmaliukh.webstore.controllers.ViewsNames.EDIT_DATA_USERS_VIEW;
import static com.vshmaliukh.webstore.controllers.ViewsNames.HOME_PAGE_VIEW;

@Controller
@RequestMapping("/" + USER_HOME)
@AllArgsConstructor
public class UserHomeController {

    ModelAttributesUtil attributesUtil;

    @GetMapping
    public ModelAndView showHomePage(ModelMap modelMap){
        return new ModelAndView(HOME_PAGE_VIEW,attributesUtil.addPageMainComponents(modelMap)); // todo create home page
    }

    @GetMapping("/" + EDIT_PAGE)
    public ModelAndView showUsersEditPage(ModelMap modelMap){
        return new ModelAndView(EDIT_DATA_USERS_VIEW,attributesUtil.addPageMainComponents(modelMap)); // todo create users edit page
    }

}
