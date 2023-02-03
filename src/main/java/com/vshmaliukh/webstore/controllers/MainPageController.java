package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.controllers.handlers.CookieHandler;
import com.vshmaliukh.webstore.controllers.handlers.ShoppingCartHandler;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.ItemRepositoryProvider;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.BaseItemRepository;
import com.vshmaliukh.webstore.services.*;
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
@RequestMapping("/main")
@AllArgsConstructor
public class MainPageController {

    final ItemService itemService;
    final UserService userService;
    final CartService cartService;
    final CartItemService cartItemService;
    final ItemRepositoryProvider itemRepositoryProvider;
    final UnauthorizedUserService unauthorizedUserService;
    ShoppingCartHandler shoppingCartHandler;

    @GetMapping
    public ModelAndView showMainPage(ModelMap modelMap) { // todo refactor template for links
        List<String> types = new ArrayList<>();
        itemRepositoryProvider.itemClassNameRepositoryMap.forEach((k, o) -> types.add(k.toUpperCase()));
//        modelMap.addAttribute("categories", categories);
        modelMap.addAttribute("types", types);
        return new ModelAndView("main-page", modelMap);
    }

    @GetMapping("/catalog/{category}/{type}")
    public ModelAndView showCatalogPage(ModelMap modelMap,
                                        @PathVariable String type,
                                        @PathVariable String category) {
        List<? extends Item> items = itemService.readAllItemsByTypeName(type);
        modelMap.addAttribute("itemList", items);
        return new ModelAndView("catalog");
    }

    @GetMapping("/catalog/{category}/{type}/{id}")
    public ModelAndView showItemPage(ModelMap modelMap,
                                     @PathVariable String category,
                                     @PathVariable String type,
                                     @PathVariable Integer id) {
        Item item = null;
        Optional<? extends Item> optionalItem = itemRepositoryProvider.getItemRepositoryByItemClassName(type).findById(id);
        if (optionalItem.isPresent()) {
            item = optionalItem.get();
        }
        modelMap.addAttribute("type", type.toLowerCase());
        modelMap.addAttribute("item", item);
        return new ModelAndView("item-page");
    }

    @PostMapping("/catalog/{category}/{type}/{id}")
    public String addItemToCartFromMainPage(@PathVariable String category,
                                            @PathVariable String type,
                                            @PathVariable Integer id,
                                            @CookieValue(defaultValue = "0") Long cartId,
                                            @RequestHeader String referer,
                                            HttpServletResponse response) {
        // todo add checking user authorization
        boolean authorization = false;

        if(!authorization){
            if(cartId==0){
                cartId = shoppingCartHandler.createNewCart().getCartId();
                Long userId = cartService.getCartByCartId(cartId).get().getCartId(); // todo remove its usage
                response.addCookie(new CookieHandler().createCookie(cartId,"cartId"));
                response.addCookie(new CookieHandler().createCookie(userId,"userId"));
            }
        }

        BaseItemRepository itemRepository = itemRepositoryProvider.getItemRepositoryByItemClassName(type);
        Optional<Item> optionalItem = itemRepository.findById(id);
        cartService.addItemToCart(optionalItem.get(), cartId);
        return "redirect:" + referer;
    }

}