package com.vshmaliukh.webstore.controllers;

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
//        return new ModelAndView(SHOPPING_CART_VIEW); // todo create cart-page
        return new ModelAndView("order"); // temporarily
    }

    @PostMapping("/add-one/{type}/{id}")
    public String incItemQuantity(@PathVariable String type,
                                        @PathVariable Integer id){
        Item item = itemRepositoryProvider.getActionsWithItemRepositoryByItemClassName(type).getItemById(id);
        cartService.addItemToCart(item,"");
        return "redirect:/" + SHOPPING_CART;
    }

    @PostMapping("/remove-one/{type}/{id}")
    public String decItemQuantity(@PathVariable String type,
                                  @PathVariable Integer id){
        // todo implement item decrementing
        return "redirect:/" + SHOPPING_CART;
    }

}
