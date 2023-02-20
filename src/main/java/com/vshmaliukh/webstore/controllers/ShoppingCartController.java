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

    @PutMapping("/add-one/{category}/{type}/{cartItemId}")
    public ResponseEntity<CartItem> incItemQuantity(@PathVariable String category,
                                                    @PathVariable String type, // todo mb optimize it
                                                    @PathVariable Long cartItemId,
                                                    @CookieValue Long userId) {
        // todo add checking authorization
        boolean authorization = false;
        CartItem changedCartItem = cartService.changeCartItemQuantityInCartOnOne(cartItemId,userId,authorization,true); // because of incrementing
        return ResponseEntity.ok(changedCartItem);
    }

    @PutMapping("/remove-one/{category}/{type}/{cartItemId}")
    public ResponseEntity<CartItem> decItemQuantity(@PathVariable String category,
                                  @PathVariable String type, // todo mb optimize it
                                  @PathVariable Long cartItemId,
                                  @CookieValue Long userId) {
        // todo implement authorization checking
        boolean authorization = false;
        CartItem changedCartItem = cartService.changeCartItemQuantityInCartOnOne(cartItemId,userId,authorization,false); // because of decrementing
        return ResponseEntity.ok(changedCartItem);
    }

    @PutMapping("/remove-all/{cartItemId}")
    public ResponseEntity<CartItem> removeItemsType(@PathVariable Long cartItemId,
                                                    @CookieValue Long cartId) {
        CartItem changedCartItem = shoppingCartHandler.removeCartItemFromCart(cartId,cartItemId);
        return ResponseEntity.ok(changedCartItem);
    }

    @DeleteMapping("/remove-all-items")
    public ResponseEntity<List<CartItem>> removeAllItemsFromCart(@CookieValue Long cartId) {
        return ResponseEntity.ok(
                cartService
                        .removeAllItemsFromCart(cartId)
                        .getItems());
    }

}
