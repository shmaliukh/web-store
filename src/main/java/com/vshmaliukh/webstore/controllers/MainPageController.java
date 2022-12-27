package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Book;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Comics;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Magazine;
import com.vshmaliukh.webstore.repositories.ItemRepositoryProvider;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.BaseItemRepository;
import com.vshmaliukh.webstore.services.CartService;
import com.vshmaliukh.webstore.services.ItemService;
import com.vshmaliukh.webstore.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.*;
import static com.vshmaliukh.webstore.controllers.ViewsNames.*;

@Controller
@RequestMapping("/"+MAIN_PAGE)
@AllArgsConstructor
public class MainPageController {

    final ItemService itemService;
    final UserService userService;
    final CartService cartService;
    final ItemRepositoryProvider itemRepositoryProvider;

    @GetMapping
    public ModelAndView showMainPage(ModelMap modelMap){
        // todo refactor template for links

        // get categories and types from db

        List<String> categories = new ArrayList<>(Arrays.asList("Literature","Another category"));
        List<List<String>> types = new ArrayList<>(Arrays.asList(
                Arrays.asList("Books","Newspapers","Comics","Magazines"),
                Arrays.asList("Another type","And another one")));
        modelMap.addAttribute("categories",categories);
        modelMap.addAttribute("types",types);
        return new ModelAndView(MAIN_PAGE_VIEW,modelMap);

    }

    @GetMapping("/" + CATALOG_PAGE + "/{type}")
    public ModelAndView showCatalogPage(ModelMap modelMap,
                                        @PathVariable String type){

        List<? extends Item> items = itemService.readAllItemsByTypeName(type);

        List<Item> itemList = getTestItemOrderList(); // for test

        modelMap.addAttribute("itemList", itemList);

//        modelMap.addAttribute("itemList", items);
        return new ModelAndView(CATALOG_VIEW);
    }

    private static List<Item> getTestItemOrderList() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Book(1, "1 book name", "book", 2, 3, true, 4, "Vlad1", new Date()));
        itemList.add(new Book(2, "2 book name", "book", 3, 4, true, 5, "Vlad2", new Date()));
        itemList.add(new Magazine(3, "Magazine name", "magazine", 4, 5, true, 6));
        itemList.add(new Comics(4, "Comics name", "comics", 5, 6, true, 7, "Some publisher"));
        return itemList;
    }

    @GetMapping("/" + CATALOG_PAGE + "/{type}/{id}")
    public ModelAndView showItemPage(ModelMap modelMap,
                                     @PathVariable String type,
                                     @PathVariable Long id){

//        Item item = itemRepositoryProvider.getActionsWithItemRepositoryByItemClassName(type).getItemById(id);

        modelMap.addAttribute("type", type.toLowerCase());

//        modelMap.addAttribute("item",item);
        Item book = new Book(1, "1 book name", "Book category", 2, 3, true, 4, "Vlad1", new Date()); // for test
        modelMap.addAttribute("item",book);
        return new ModelAndView(ITEM_PAGE_VIEW,modelMap);
    }

    @PostMapping("/" + CATALOG_PAGE + "/{type}/{id}")
    public String addToCart(@PathVariable String type,
                             @PathVariable Integer id,
                             @RequestHeader String referer){
        BaseItemRepository itemRepository = itemRepositoryProvider.getItemRepositoryByItemClassName(type);
        Item item = itemRepository.getById(id);
        cartService.addItemToCart(item,"username"); // todo implement username usage
        return "redirect:" + referer;
    }

}