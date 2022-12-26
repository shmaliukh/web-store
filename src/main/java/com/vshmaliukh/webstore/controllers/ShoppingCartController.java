package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.model.Cart;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.ActionsWithItemRepositoryProvider;
import com.vshmaliukh.webstore.services.CartService;
import com.vshmaliukh.webstore.services.ItemService;
import com.vshmaliukh.webstore.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.SHOPPING_CART;
import static com.vshmaliukh.webstore.controllers.ViewsNames.SHOPPING_CART_VIEW;

@Controller
@RequestMapping("/" + SHOPPING_CART)
@AllArgsConstructor
public class ShoppingCartController {

    final ItemService itemService;
    final UserService userService;
    final CartService cartService;
    final ActionsWithItemRepositoryProvider itemRepositoryProvider;

    @GetMapping
    public ModelAndView showCartPage(ModelMap modelMap){
//        List<Cart> carts = cartService.getCartsByUserId(userService.readUserIdByName("")); // todo add username usage
        List<Cart> carts = getTestCarts();
        modelMap.addAttribute("items",carts);
        modelMap.addAttribute("totalItems",carts.size()+1);
        int totalPrice = 0;
        for (Cart cart : carts) {
            totalPrice = totalPrice
                    + itemRepositoryProvider
                    .getActionsWithItemRepositoryByItemClassName(cart.getCategory())
                    .getItemById(cart.getItemId()).getPrice();
        }
        modelMap.addAttribute("totalPrice",totalPrice);
        return new ModelAndView(SHOPPING_CART_VIEW);
    }

    @PostMapping("/add-one/{type}/{id}")
    public String incItemQuantity(@PathVariable String type,
                                        @PathVariable Integer id){
        Item item = itemRepositoryProvider.getActionsWithItemRepositoryByItemClassName(type).getItemById(id);
        cartService.addItemToCart(item,"username"); // todo implement username usage
        return "redirect:/" + SHOPPING_CART;
    }

    @PostMapping("/remove-one/{type}/{id}")
    public String decItemQuantity(@PathVariable String type,
                                  @PathVariable Integer id){
        Item item = itemRepositoryProvider.getActionsWithItemRepositoryByItemClassName(type).getItemById(id);
        cartService.decItemQuantityInCart(item,"username"); // todo implement username usage
        return "redirect:/" + SHOPPING_CART;
    }

    public List<Cart> getTestCarts(){
        List<Cart> carts = new ArrayList<>();

        Cart first = new Cart();
        first.setCartId(Long.getLong("1"));
        first.setUserId(Long.getLong("1"));
        first.setCategory("book");
        first.setItemQuantity(4);
        first.setPrice(1000);

        Cart second = new Cart();
        second.setCartId(Long.getLong("1"));
        second.setUserId(Long.getLong("1"));
        second.setCategory("book");
        second.setItemQuantity(4);
        second.setPrice(1000);
        carts.add(first);
        carts.add(second);
        return carts;
    }

}
