package com.vshmaliukh.webstore.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.*;
import static com.vshmaliukh.webstore.controllers.ModelAttributesConstants.PRICE;
import static com.vshmaliukh.webstore.controllers.ViewsNames.*;

@Controller
@RequestMapping("/"+MAIN_PAGE)
@AllArgsConstructor
public class MainPageController {

    ModelAttributesUtil attributesUtil;

    @GetMapping
    public ModelAndView showMainPage(ModelMap modelMap){
        // todo create main page and draw icons
        return new ModelAndView(MAIN_PAGE_VIEW,attributesUtil.addPageMainComponents(modelMap));
    }

    @GetMapping("/" + CATALOG_PAGE)
    public ModelAndView showCatalogPage(ModelMap modelMap){
        modelMap.addAttribute(PRICE,"$666.00");// todo create constants and tiles genering for catalog
        return new ModelAndView(CATALOG_VIEW,attributesUtil.addPageMainComponents(modelMap));
    }

    @GetMapping("/" + CATALOG_PAGE + "/" + CATEGORY_PAGE + "/" + ITEM_PAGE)
    public ModelAndView showItemPage(ModelMap modelMap){
        return new ModelAndView(ITEM_PAGE_VIEW,attributesUtil.addPageMainComponents(modelMap)); // todo create item-page
    }

}