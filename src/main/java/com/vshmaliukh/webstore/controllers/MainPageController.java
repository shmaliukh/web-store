package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.model.items.literature_item_imp.Book;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
        modelMap.addAttribute(PRICE,"$666.00");// todo create constants and tiles genering for catalog
        return new ModelAndView(CATALOG_VIEW);
    }

    @GetMapping("/" + CATALOG_PAGE + "/{" + CATEGORY_PAGE + "}/{" + ITEM_PAGE + "}/{id}")
    public ModelAndView showItemPage(ModelMap modelMap,
                                     @PathVariable String category,
                                     @PathVariable String item,
                                     @PathVariable Long id){ // or Integer - idk

        // getting item from db by item name and id and category name?

        Book book = new Book();
        String bookInfo = book.getName() + " - " + book.getPrice();   // for catalog tiles
        String bookDetailInfo = book.getAuthor() + " - " + book.getPages() + " - " + book.getDateOfIssue() + " - "; // info for short description
        String bookPrice = Integer.toString(book.getPrice()); // separate for price ?
        String bookName = book.getName();

        return new ModelAndView(ITEM_PAGE_VIEW); // todo create item-page and implement item details printing
    }

}