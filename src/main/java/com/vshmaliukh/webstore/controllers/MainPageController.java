package com.vshmaliukh.webstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/main")
public class MainPageController {

    @GetMapping
    public ModelAndView showMainPage(ModelMap modelMap){
        // todo create main page and draw icons
        return new ModelAndView("main-page",addPageMainComponents(modelMap));
    }

    @GetMapping("/catalog")
    public ModelAndView showCatalogPage(ModelMap modelMap){
        modelMap.addAttribute("price","$666.00");// todo create constants and tiles genering for catalog
        ModelAndView modelAndView = new ModelAndView("catalog",addPageMainComponents(modelMap));

        return modelAndView;
    }

    ModelMap addPageMainComponents(ModelMap modelMap){
        modelMap.addAttribute("header","header"); // todo extract method to any other class
        modelMap.addAttribute("footer","footer");
        return modelMap;
    }

    @GetMapping("/catalog/category/item")
    public ModelAndView showItemPage(ModelMap modelMap){
        return new ModelAndView("item-page");
    }


}