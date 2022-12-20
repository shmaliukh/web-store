package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Book;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Comics;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Magazine;
import com.vshmaliukh.webstore.repositories.ActionsWithItemRepositoryProvider;
import com.vshmaliukh.webstore.services.ItemService;
import com.vshmaliukh.webstore.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.*;
import static com.vshmaliukh.webstore.controllers.ModelAttributesConstants.PRICE;
import static com.vshmaliukh.webstore.controllers.ViewsNames.*;

@Controller
@RequestMapping("/"+MAIN_PAGE)
@AllArgsConstructor
public class MainPageController {

    final ItemService itemService;
    final UserService userService;
    final ActionsWithItemRepositoryProvider itemRepositoryProvider;

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
        List<String> urls = genURLForItemsPages(items);
        List<Item> itemList = getTestItemOrderList(); // for test
        List<String> urlsTest = genURLForItemsPages(itemList);
        modelMap.addAttribute("itemList", itemList);
        modelMap.addAttribute("urls",urlsTest);




//        modelMap.addAttribute("itemList", items);
//        modelMap.addAttribute("urls",urls);
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
                                     @PathVariable Integer id){

//        Item item = itemRepositoryProvider.getActionsWithItemRepositoryByItemClassName(type).getItemById(id);

        modelMap.addAttribute("type", type.toLowerCase());

//        modelMap.addAttribute("item",item);
        Item book = new Book(1, "1 book name", "Book category", 2, 3, true, 4, "Vlad1", new Date()); // for test
        modelMap.addAttribute("item",book);
        modelMap.addAttribute("href","/" + MAIN_PAGE + "/" + CATALOG_PAGE + "/" + type + "/" + id);
        return new ModelAndView(ITEM_PAGE_VIEW,modelMap);
    }

    @PostMapping("/" + CATALOG_PAGE + "/{type}/{id}")
    public String addToOrder(@PathVariable String type,
                             @PathVariable Integer id,
                             @RequestHeader String referer){
        // todo implement adding
        return "redirect:" + referer;
    }

    public List<String> genURLForItemsPages(List<? extends Item> items){

        List<String> urls = new ArrayList<>();
        String baseURL = "/" + MAIN_PAGE + "/" + CATALOG_PAGE + "/";
        items.forEach(item->urls.add(baseURL+item.getCategory() + "/" + item.getId()));
        return urls;
    }
}