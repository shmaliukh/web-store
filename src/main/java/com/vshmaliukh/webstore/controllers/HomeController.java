package com.vshmaliukh.webstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.PAGE_HOME;

@Controller
@RequestMapping("/" + PAGE_HOME)
public class HomeController {

    @GetMapping
    public ModelAndView doGet(ModelMap modelMap) {
        return new ModelAndView(PAGE_HOME, modelMap);
    }

}
