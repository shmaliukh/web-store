package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.controllers.handlers.CookieHandler;
import com.vshmaliukh.webstore.model.Cart;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.ItemRepositoryProvider;
import com.vshmaliukh.webstore.services.CartService;
import com.vshmaliukh.webstore.services.ItemService;
import com.vshmaliukh.webstore.services.UnauthorizedUserService;
import com.vshmaliukh.webstore.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/shopping-cart")
@AllArgsConstructor
public class ShoppingCartController {

    final CookieHandler cookieHandler = new CookieHandler();

    final ItemService itemService;
    final UserService userService;
    final CartService cartService;
    final ItemRepositoryProvider itemRepositoryProvider;
    final UnauthorizedUserService unauthorizedUserService;

    @GetMapping
    public ModelAndView showCartPage(ModelMap modelMap,
                                     HttpServletResponse response,
                                     @CookieValue(required = false, defaultValue = "0") Long userId) {
        if (userId == 0) {
            userId = unauthorizedUserService.createUnauthorizedUser().getId();
            response.addCookie(cookieHandler.createUserIdCookie(userId));
        }
        List<Cart> carts = cartService.getCartsByUserId(unauthorizedUserService.getUserById(userId).getId());
        List<Item> items = new ArrayList<>();
        for (Cart cart : carts) {
            Item item = itemRepositoryProvider.getItemRepositoryByItemClassName(cart.getCategory())
                    .getById(cart.getItemId());
            item.setPrice(item.getPrice()*item.getQuantity());
            items.add(item);
        }
        int totalCount = 0;
        int totalPrice = 0;
        for (Item item : items) {
            totalPrice = totalPrice + item.getPrice();
        }
        for (Item item : items) {
            totalCount = totalCount + item.getQuantity();
        }
        modelMap.addAttribute("items", items);
        modelMap.addAttribute("totalItems", totalCount);
        modelMap.addAttribute("totalPrice", totalPrice);
        return new ModelAndView("shopping-cart");
    }

    @GetMapping("/add-one/{type}/{id}")
    public String incItemQuantity(@PathVariable String type,
                                  @PathVariable Integer id,
                                  @CookieValue Long userId) {
        final Long finalUserId = userId;
        Optional<? extends Item> optionalItem = itemRepositoryProvider.getItemRepositoryByItemClassName(type).findById(id);
        optionalItem.ifPresent(item -> cartService.addItemToCart(item, finalUserId));
        return "redirect:/shopping-cart";
    }

    @GetMapping("/remove-one/{type}/{id}")
    public String decItemQuantity(@PathVariable String type,
                                  @PathVariable Integer id,
                                  @CookieValue Long userId) {
        Optional<? extends Item> optionalItem = itemRepositoryProvider.getItemRepositoryByItemClassName(type).findById(id);
        optionalItem.ifPresent(item -> cartService.decItemQuantityInCart(item, userId));
        return "redirect:/shopping-cart";
    }

}
