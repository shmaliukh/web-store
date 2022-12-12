package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Book;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Comics;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Magazine;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.*;
import static com.vshmaliukh.webstore.controllers.ModelAttributesConstants.PRICE;
import static com.vshmaliukh.webstore.controllers.ViewsNames.*;

@Controller
@RequestMapping("/"+MAIN_PAGE)
@AllArgsConstructor
public class MainPageController {

    @GetMapping
    public ModelAndView showMainPage(ModelMap modelMap){
        // todo create main page and draw icons
        return new ModelAndView(MAIN_PAGE_VIEW);
    }

    @GetMapping("/" + CATALOG_PAGE)
    public ModelAndView showCatalogPage(ModelMap modelMap){
        // todo
        List itemList = getTestItemOrderList();
        modelMap.addAttribute("itemList", itemList);
        modelMap.addAttribute(PRICE,"$666.00");// todo create constants and tiles genering for catalog
        return new ModelAndView(CATALOG_VIEW);
    }

    private static List<Item> getTestItemOrderList() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Book(1, "1 book name", "Book category", 2, 3, true, 4, "Vlad1", new Date()));
        itemList.add(new Book(2, "2 book name", "Book category   ", 3, 4, true, 5, "Vlad2", new Date()));
        itemList.add(new Magazine(3, "Magazine name", "Magazine category", 4, 5, true, 6));
        itemList.add(new Comics(4, "Comics name", "Comics category", 5, 6, true, 7, "Some publisher"));
        return itemList;
    }

    @GetMapping("/" + CATALOG_PAGE + "/{" + CATEGORY_PAGE + "}/{id}")
    public ModelAndView showItemPage(ModelMap modelMap,
                                     @PathVariable String category,
                                     @PathVariable Long id){ // or Integer - idk

        // getting item from db by item id and category name?

        Book book = new Book();
        String itemInfo = book.getName() + " - " + book.getPrice();   // for catalog tiles
        String itemDetailInfo = book.getAuthor() + " - " + book.getPages() + " - " + book.getDateOfIssue() + " - "; // info for short description
        String itemPrice = Integer.toString(book.getPrice()); // separate for price ?
        String itemName = book.getName();

        modelMap.addAttribute("itemName", itemName);
        modelMap.addAttribute("itemPrice",itemPrice);
        modelMap.addAttribute("itemDetailInfo",itemDetailInfo);


        return new ModelAndView(ITEM_PAGE_VIEW); // todo create item-page and implement item details printing
    }

}