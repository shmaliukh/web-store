package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.controllers.handlers.CookieHandler;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.ItemRepositoryProvider;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.BaseItemRepository;
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
import java.util.*;


@Controller
@RequestMapping("/main")
@AllArgsConstructor
public class MainPageController {

    final ItemService itemService;
    final UserService userService;
    final CartService cartService;
    final ItemRepositoryProvider itemRepositoryProvider;
    final UnauthorizedUserService unauthorizedUserService;

    @GetMapping
    public ModelAndView showMainPage(ModelMap modelMap) {
        // todo refactor template for links

        // get categories and types from db

        List<String> categories = new ArrayList<>(Arrays.asList("Literature", "Another category"));
        List<List<String>> types = new ArrayList<>(Arrays.asList(
                Arrays.asList("Books", "Newspapers", "Comics", "Magazines"),
                Arrays.asList("Another type", "And another one")));
        modelMap.addAttribute("categories", categories);
        modelMap.addAttribute("types", types);
        return new ModelAndView("main-page",modelMap);
    }

    @GetMapping("/catalog/{type}")
    public ModelAndView showCatalogPage(ModelMap modelMap,
                                  @PathVariable String type) {
        List<? extends Item> items = itemService.readAllItemsByTypeName(type);

        List<Item> itemList = Collections.emptyList();
//                getTestItemOrderList(); // for test

        modelMap.addAttribute("itemList", itemList);

//        modelMap.addAttribute("itemList", items);
        return new ModelAndView("catalog");
    }

    @GetMapping("/catalog/{type}/{id}")
    public ModelAndView showItemPage(ModelMap modelMap,
                               @PathVariable String type,
                               @PathVariable Long id) {
//        Item item = itemRepositoryProvider.getActionsWithItemRepositoryByItemClassName(type).getItemById(id);

        modelMap.addAttribute("type", type.toLowerCase());

//        modelMap.addAttribute("item",item);
//        FIXME
//        Item book = new Book(1, "1 book name", "Book category", 2, 3, true, 4, "Vlad1", new Date()); // for test
//        modelMap.addAttribute("item",book);
        return new ModelAndView("item-page",modelMap);
    }

    @PostMapping("/catalog/{type}/{id}")
    public String addToCart(@PathVariable String type,
                            @PathVariable Integer id,
                            @RequestHeader String referer,
                            @CookieValue(required = false, defaultValue = "0") Long userId,
                            HttpServletResponse response) {
        if (userId == 0) {
            userId = unauthorizedUserService.createUnauthorizedUser().getId();
            response.addCookie(new CookieHandler().createUserIdCookie(userId));
        }
        final Long finalUserId = userId;
        BaseItemRepository itemRepository = itemRepositoryProvider.getItemRepositoryByItemClassName(type);
        Optional<Item> optionalItem = itemRepository.findById(id);
        optionalItem.ifPresent(item -> cartService.addItemToCart(item, finalUserId));
        return "redirect:" + referer;
    }

}