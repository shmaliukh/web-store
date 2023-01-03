package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.controllers.handlers.CookieHandler;
import com.vshmaliukh.webstore.model.Cart;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Book;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Comics;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Magazine;
import com.vshmaliukh.webstore.repositories.ItemRepositoryProvider;
import com.vshmaliukh.webstore.services.CartService;
import com.vshmaliukh.webstore.services.ItemService;
import com.vshmaliukh.webstore.services.UnauthorizedUserService;
import com.vshmaliukh.webstore.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.SHOPPING_CART;
import static com.vshmaliukh.webstore.controllers.ViewsNames.SHOPPING_CART_VIEW;

@Slf4j
@Controller
@RequestMapping("/" + SHOPPING_CART)
@AllArgsConstructor
public class ShoppingCartController {

    CookieHandler cookieHandler;

    final ItemService itemService;
    final UserService userService;
    final CartService cartService;
    final ItemRepositoryProvider itemRepositoryProvider;
    final UnauthorizedUserService unauthorizedUserService;

    @GetMapping
    public ModelAndView showCartPage(ModelMap modelMap, HttpServletResponse response,
                                     @CookieValue(required = false,defaultValue = "0") Long userId){
        List<Item> testItems = getTestItemOrderList(); // for tests
        if(userId==0){
            userId = unauthorizedUserService.createUnauthorizedUser().getId();
            response.addCookie(cookieHandler.createUserIdCookie(userId));
        }
//        List<Cart> carts = cartService.getCartsByUserId(userService.getUserById(id).getId()); // todo uncomment when test items will be removed
//        List<Item> items = new ArrayList<>();
//        for (Cart cart : carts) {
//            Item item = itemRepositoryProvider.getItemRepositoryByItemClassName(cart.getCategory())
//                    .getById(cart.getItemId());
//            item.setPrice(item.getPrice()*item.getQuantity());
//            items.add(item);
//        }

        for (Item item : testItems) { // for tests
            item.setPrice(item.getPrice()* item.getQuantity());
        }

        int totalCount = 0;
        int totalPrice = 0;

//        for (Item item : items) {
//            totalPrice = totalPrice + item.getPrice();
//        }
//        for (Item item : items) {
//            totalCount = totalCount + item.getQuantity();
//        }

        for (Item item : testItems) {  // for tests
            totalPrice = totalPrice + item.getPrice();
        }
        for (Item item : testItems) { // for tests
            totalCount = totalCount + item.getQuantity();
        }

        modelMap.addAttribute("items",testItems);
        modelMap.addAttribute("totalItems",totalCount);
        modelMap.addAttribute("totalPrice",totalPrice);
        return new ModelAndView(SHOPPING_CART_VIEW);
    }

    @GetMapping("/add-one/{type}/{id}")
    public String incItemQuantity(@PathVariable String type,
                                  @PathVariable Integer id,
                                  @CookieValue Long userId){
        try {
            final Long finalUserId = userId;
            Optional<? extends Item> optionalItem = itemRepositoryProvider.getItemRepositoryByItemClassName(type).findById(id);
            optionalItem.ifPresent(item -> cartService.addItemToCart(item, finalUserId));
            return "redirect:/" + SHOPPING_CART;
        } catch (Exception exception){
            log.warn(exception.getMessage(),ShoppingCartController.class);
            return "redirect:/error"; // todo implement error page mapping
        }
    }

    @GetMapping("/remove-one/{type}/{id}")
    public String decItemQuantity(@PathVariable String type,
                                  @PathVariable Integer id,
                                  @CookieValue Long userId){
        try {
            Optional<? extends Item> optionalItem = itemRepositoryProvider.getItemRepositoryByItemClassName(type).findById(id);
        optionalItem.ifPresent(item -> cartService.decItemQuantityInCart(item, userId));
            return "redirect:/" + SHOPPING_CART;
        } catch (Exception exception){
            log.warn(exception.getMessage(),ShoppingCartController.class);
            return "redirect:/error"; // todo implement error page mapping
        }
    }

    public List<Cart> getTestCarts() {
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

    private static List<Item> getTestItemOrderList() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Book(1, "1 book name", "book", 2, 3, true, 4, "Vlad1", new Date()));
        itemList.add(new Book(2, "2 book name", "book", 3, 4, true, 5, "Vlad2", new Date()));
        itemList.add(new Magazine(3, "Magazine name", "magazine", 4, 5, true, 6));
        itemList.add(new Comics(4, "Comics name", "comics", 5, 6, true, 7, "Some publisher"));
        return itemList;
    }



}
