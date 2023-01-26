package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.controllers.handlers.CookieHandler;
import com.vshmaliukh.webstore.controllers.handlers.ShoppingCartHandler;
import com.vshmaliukh.webstore.model.carts.Cart;
import com.vshmaliukh.webstore.model.carts.UnauthorizedUserCart;
import com.vshmaliukh.webstore.model.items.CartItem;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.ItemRepositoryProvider;
import com.vshmaliukh.webstore.services.CartService;
import com.vshmaliukh.webstore.services.ItemService;
import com.vshmaliukh.webstore.services.UnauthorizedUserService;
import com.vshmaliukh.webstore.services.UserService;
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
import java.util.Optional;

@Controller
@RequestMapping("/shopping-cart")
@AllArgsConstructor
public class ShoppingCartController {

    final ShoppingCartHandler shoppingCartHandler;
    final CookieHandler cookieHandler = new CookieHandler();

    final ItemService itemService;

    final CartService cartService;
    final ItemRepositoryProvider itemRepositoryProvider;

    final UserService userService;
    final UnauthorizedUserService unauthorizedUserService;

    @GetMapping
    public ModelAndView showCartPage(ModelMap modelMap,
                                     HttpServletResponse response,
                                     @CookieValue(required = false, defaultValue = "0") Long userId) {

        // userId can be 0 if user authorized, or it can be a new user - then check authorization

        // todo add checking for user authorizing

        boolean authorization = false;
        if(!authorization) {
//            unauthorizedUserService.removeOldUsers(); // todo refactor usage of old unauthorized users removing
            if (userId == 0) {
                UnauthorizedUserCart unauthorizedUserCart  = new UnauthorizedUserCart();
                cartService.addNewCart(unauthorizedUserCart);
                userId = unauthorizedUserService
                        .createUnauthorizedUser(unauthorizedUserCart)
                        .getId();
                response.addCookie(
                        cookieHandler.createUserIdCookie(userId)
                );
            }
            Cart cart = cartService.getCartByUserId(unauthorizedUserService.getUserById(userId).getId(),authorization);
            if(cart!=null) {
                List<CartItem> cartItems = cart.getItems();
                int totalCount = shoppingCartHandler.countAllItemsQuantity(cartItems);
                int totalPrice = shoppingCartHandler.countAllItemsPrice(cartItems);
                modelMap.addAttribute("items", cartItems);
                modelMap.addAttribute("totalItems", totalCount);
                modelMap.addAttribute("totalPrice", totalPrice);
            }
        } else {
            if(userId!=0){
                shoppingCartHandler.changeCartToAuthorized(
                        cartService.getCartByUserId(userId,false),
                        userService.getUserById(userId)); // todo check authorized user id
            }
        }
        return new ModelAndView("shopping-cart");
    }

    @GetMapping("/add-one/{type}/{id}")
    public String incItemQuantity(@PathVariable String type,
                                  @PathVariable Integer id,
                                  @CookieValue Long userId) {

        // todo add checking authorization

        boolean authorization = false;
        final Long finalUserId = userId;
        Optional<? extends Item> optionalItem = itemRepositoryProvider.getItemRepositoryByItemClassName(type).findById(id);
        optionalItem.ifPresent(item -> cartService.addItemToCart(item, finalUserId,authorization));
        return "redirect:/shopping-cart";
    }

    @GetMapping("/remove-one/{type}/{id}")
    public String decItemQuantity(@PathVariable String type,
                                  @PathVariable Integer id,
                                  @CookieValue Long userId) {
        // todo implement authorization checking
        boolean authorization = false;
        Optional<? extends Item> optionalItem = itemRepositoryProvider.getItemRepositoryByItemClassName(type).findById(id);
        optionalItem.ifPresent(item -> cartService.decItemQuantityInCart(item, userId, authorization));
        return "redirect:/shopping-cart";
    }
}
