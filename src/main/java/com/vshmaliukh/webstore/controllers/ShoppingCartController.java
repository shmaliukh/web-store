package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.controllers.handlers.ShoppingCartHandler;
import com.vshmaliukh.webstore.model.items.CartItem;
import com.vshmaliukh.webstore.services.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/shopping-cart")
@AllArgsConstructor
public class ShoppingCartController {

    final ShoppingCartHandler shoppingCartHandler;

    @GetMapping
    public ModelAndView showCartPage(ModelMap modelMap,
                                     HttpServletResponse response,
                                     @CookieValue(defaultValue = "0", name = "cartId") Long cartId) {

        // todo add checking for user authorizing
        boolean authorization = false;

        modelMap.addAttribute("items", shoppingCartHandler.showShoppingCart(authorization,cartId,response));
        return new ModelAndView("shopping-cart", modelMap);
    }

}
