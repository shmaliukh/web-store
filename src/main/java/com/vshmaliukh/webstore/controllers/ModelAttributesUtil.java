package com.vshmaliukh.webstore.controllers;

import org.springframework.ui.ModelMap;

import static com.vshmaliukh.webstore.controllers.ModelAttributesConstants.*;

public class ModelAttributesUtil {

    ModelMap addPageMainComponents(ModelMap modelMap){
        modelMap.addAttribute(HEADER,HEADER);
        modelMap.addAttribute(FOOTER,FOOTER);
        return modelMap;
    }

}
