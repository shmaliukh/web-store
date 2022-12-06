package com.vshmaliukh.webstore.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/home")
@AllArgsConstructor
public class UserHomeController {

    ModelAttributesUtil attributesUtil;

    @GetMapping
    public ModelAndView showHomePage(ModelMap modelMap){
        return new ModelAndView("home-page",attributesUtil.addPageMainComponents(modelMap)); // todo create home page
    }

}
