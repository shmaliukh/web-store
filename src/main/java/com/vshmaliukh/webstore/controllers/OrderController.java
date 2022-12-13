package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Book;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Comics;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Magazine;
import com.vshmaliukh.webstore.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.ORDER_PAGE;
import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.HOME_PAGE;


@Slf4j
@Controller
@RequestMapping("/" + ORDER_PAGE)
public class OrderController {

    public static final String POST_MAPPING_ORDER_STATUS_STR = "post mapping order controller";
    final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    private static List<Item> getTestItemOrderList() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Book(1, "1 book name", "Book category", 2, 3, true, 4, "Vlad1", new Date()));
        itemList.add(new Book(2, "2 book name", "Book category   ", 3, 4, true, 5, "Vlad2", new Date()));
//        itemList.add(new Magazine(3, "Magazine name", "Magazine category", 4, 5, true, 6));
        itemList.add(new Comics(4, "Comics name", "Comics category", 5, 6, true, 7, "Some publisher"));
        return itemList;
    }

    @GetMapping
    public ModelAndView doGet(@CookieValue(defaultValue = "1") long userId,
                              ModelMap modelMap) {
        List<Item> itemList = getTestItemOrderList();
        int totalPrice = orderService.calcOrderTotalSumByUserId(userId);

        modelMap.addAttribute("itemList", itemList);
        modelMap.addAttribute("totalPrice", totalPrice);
        return new ModelAndView(ORDER_PAGE, modelMap);
    }

    @PostMapping
    public ModelAndView doPost(@CookieValue(defaultValue = "1") long userId,
                               ModelMap modelMap) {
        // TODO update user data
        orderService.changeOrderStatus(userId, POST_MAPPING_ORDER_STATUS_STR);
        return new ModelAndView("redirect:/" + HOME_PAGE, modelMap);
    }

}