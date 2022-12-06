package com.vshmaliukh.webstore.controllers;

import org.springframework.ui.ModelMap;

public class ModelAttributesUtil {

    ModelMap addPageMainComponents(ModelMap modelMap){
        modelMap.addAttribute("header","header");
        modelMap.addAttribute("footer","footer");
        return modelMap;
    }

}
