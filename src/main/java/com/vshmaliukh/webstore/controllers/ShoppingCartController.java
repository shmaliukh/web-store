package com.vshmaliukh.webstore.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.SHOPPING_CART;
import static com.vshmaliukh.webstore.controllers.ViewsNames.SHOPPING_CART_VIEW;

@Controller
@RequestMapping("/" + SHOPPING_CART)
@AllArgsConstructor
public class ShoppingCartController {

    ModelAttributesUtil attributesUtil;

    @GetMapping
    public ModelAndView showCartPage(ModelMap modelMap){
        return new ModelAndView(SHOPPING_CART_VIEW,attributesUtil.addPageMainComponents(modelMap)); // todo create cart-page
    }

    // todo create post method

}
