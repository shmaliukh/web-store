package com.vshmaliukh.webstore.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/cart")
@AllArgsConstructor
public class ShoppingCartController {

    ModelAttributesUtil attributesUtil;

    @GetMapping("/shopping-cart")
    public ModelAndView showCartPage(ModelMap modelMap){
        return new ModelAndView("cart",attributesUtil.addPageMainComponents(modelMap)); // todo create cart-page
    }

    // todo create post method

}
