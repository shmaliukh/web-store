package com.vshmaliukh.webstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/main")
public class MainPageController {

    @GetMapping
    public ModelAndView showMainPage(){
        // todo create main page
        return new ModelAndView("main-page");
    }

    @GetMapping("/catalog")
    public ModelAndView showCatalogPage(ModelMap modelMap){
        modelMap.addAttribute("price","$666.00"); // todo create constants and tiles genering for catalog
        ModelAndView modelAndView = new ModelAndView("catalog",modelMap);

        return modelAndView;
    }


}