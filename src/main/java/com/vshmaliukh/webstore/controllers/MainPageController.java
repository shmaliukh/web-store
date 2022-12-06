package com.vshmaliukh.webstore.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/main")
@AllArgsConstructor
public class MainPageController {

    ModelAttributesUtil attributesUtil;

    @GetMapping
    public ModelAndView showMainPage(ModelMap modelMap){
        // todo create main page and draw icons
        return new ModelAndView("main-page",attributesUtil.addPageMainComponents(modelMap));
    }

    @GetMapping("/catalog")
    public ModelAndView showCatalogPage(ModelMap modelMap){
        modelMap.addAttribute("price","$666.00");// todo create constants and tiles genering for catalog
        return new ModelAndView("catalog",attributesUtil.addPageMainComponents(modelMap));
    }

    @GetMapping("/catalog/category/item")
    public ModelAndView showItemPage(ModelMap modelMap){
        return new ModelAndView("item-page",attributesUtil.addPageMainComponents(modelMap)); // todo create item-page
    }

}