package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.controllers.handlers.ShoppingCartHandler;
import com.vshmaliukh.webstore.services.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/shopping-cart")
@AllArgsConstructor
public class ShoppingCartController {

    final ShoppingCartHandler shoppingCartHandler;

    final CartService cartService;

    @GetMapping
    public ModelAndView showCartPage(ModelMap modelMap,
                                     HttpServletResponse response,
                                     @CookieValue(defaultValue = "0", name = "cartId") Long cartId) {

        // todo add checking for user authorizing
        boolean authorization = false;

        modelMap = shoppingCartHandler.showShoppingCart(authorization,cartId,modelMap,response);
        return new ModelAndView("shopping-cart", modelMap);
    }

    @GetMapping("/add-one/{category}/{type}/{cartItemId}")
    public String incItemQuantity(@PathVariable String category,
                                  @PathVariable String type, // todo mb optimize it
                                  @PathVariable Long cartItemId,
                                  @CookieValue Long userId) {
        // todo add checking authorization
        boolean authorization = false;
        cartService.changeCartItemQuantityInCartOnOne(cartItemId,userId,authorization,true); // because of incrementing
        return "redirect:/shopping-cart";
    }

    @GetMapping("/remove-one/{category}/{type}/{cartItemId}")
    public String decItemQuantity(@PathVariable String category,
                                  @PathVariable String type, // todo mb optimize it
                                  @PathVariable Long cartItemId,
                                  @CookieValue Long userId) {
        // todo implement authorization checking
        boolean authorization = false;
        cartService.changeCartItemQuantityInCartOnOne(cartItemId,userId,authorization,false); // because of decrementing
        return "redirect:/shopping-cart";
    }

    @GetMapping("/remove-all/{cartItemId}")
    public String removeItemsType(@PathVariable Long cartItemId,
                                  @CookieValue Long cartId) {
        cartService.getCartByCartId(cartId).ifPresent(cart -> cartService.removeOneCartItemsTypeFromCart(cart,cartItemId));
        return "redirect:/shopping-cart";
    }

    @GetMapping("/remove-all-items")
    public String removeAllItemsFromCart(@CookieValue Long cartId) {
        cartService.removeAllItemsFromCart(cartId);
        return "redirect:/shopping-cart";
    }

}
