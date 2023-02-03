package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.controllers.handlers.CookieHandler;
import com.vshmaliukh.webstore.controllers.handlers.ShoppingCartHandler;
import com.vshmaliukh.webstore.model.UnauthorizedUser;
import com.vshmaliukh.webstore.model.carts.Cart;
import com.vshmaliukh.webstore.model.carts.UnauthorizedUserCart;
import com.vshmaliukh.webstore.model.items.CartItem;
import com.vshmaliukh.webstore.repositories.ItemRepositoryProvider;
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
import java.util.List;

@Controller
@RequestMapping("/shopping-cart")
@AllArgsConstructor
public class ShoppingCartController {

    final ShoppingCartHandler shoppingCartHandler;
    final CookieHandler cookieHandler = new CookieHandler();

    final ItemService itemService;

    final CartItemService cartItemService;

    final CartService cartService;
    final ItemRepositoryProvider itemRepositoryProvider;

    final UserService userService;
    final UnauthorizedUserService unauthorizedUserService;

    @GetMapping
    public ModelAndView showCartPage(ModelMap modelMap,
                                     HttpServletResponse response,
//                                     HttpServletRequest request,

                                     @CookieValue(defaultValue = "0", name = "cartId") Long cartId) {

        // userId can be 0 if user authorized, or it can be a new user - then check authorization

        // todo add checking for user authorizing
        boolean authorization = false;

        if(!authorization){
            if(cartId==null){
                cartId = shoppingCartHandler.createNewCart().getCartId();
                response.addCookie(
                        cookieHandler.createCookie(cartId,"cartId")
                );
            }

            Cart cart = cartService.getCartByCartId(cartId).get();
            List<CartItem> cartItems = cart.getItems();
            int totalCount = shoppingCartHandler.countAllItemsQuantity(cartItems);
            int totalPrice = shoppingCartHandler.countAllItemsPrice(cartItems);
            modelMap.addAttribute("items", cartItems);
            modelMap.addAttribute("totalItems", totalCount);
            modelMap.addAttribute("totalPrice", totalPrice);
        }
        return new ModelAndView("shopping-cart");
    }

    @GetMapping("/add-one/{category}/{type}/{cartItemId}")
    public String incItemQuantity(@PathVariable String category,
                                  @PathVariable String type, // todo mb optimize it
                                  @PathVariable Integer cartItemId,
                                  @CookieValue Long userId) {
        // todo add checking authorization
        boolean authorization = false;
        cartService.changeCartItemQuantityInCartOnOne(cartItemId,userId,authorization,true); // because of incrementing
        return "redirect:/shopping-cart";
    }

    @GetMapping("/remove-one/{category}/{type}/{cartItemId}")
    public String decItemQuantity(@PathVariable String category,
                                  @PathVariable String type, // todo mb optimize it
                                  @PathVariable Integer cartItemId,
                                  @CookieValue Long userId) {
        // todo implement authorization checking
        boolean authorization = false;
        cartService.changeCartItemQuantityInCartOnOne(cartItemId,userId,authorization,false); // because of decrementing
        return "redirect:/shopping-cart";
    }

    @GetMapping("/remove-all/{cartItemId}")
    public String removeItemsType(
                                  @PathVariable Integer cartItemId,
                                  @CookieValue(defaultValue = "0") Long cartId) {
        // todo implement authorization checking
        boolean authorization = false;
        cartService.removeOneCartItemsTypeFromCart(cartService.getCartByCartId(cartId).get(),cartItemId);
        return "redirect:/shopping-cart";
    }

}
