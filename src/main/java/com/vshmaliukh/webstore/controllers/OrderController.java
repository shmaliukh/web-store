package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.model.Order;
import com.vshmaliukh.webstore.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.HOME_PAGE;
import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.ORDER_PAGE;


@Slf4j
@Controller
@RequestMapping("/" + ORDER_PAGE)
public class OrderController {

    public static final String POST_MAPPING_ORDER_STATUS_STR = "post mapping order controller";
    final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ModelAndView doGet(@CookieValue(defaultValue = "1") long userId,
                              ModelMap modelMap) {
//        FIXME
//        List<Item> itemList = orderService.;
//        int totalPrice = orderService.calcOrderTotalSum(userId);

//        FIXME
//        modelMap.addAttribute("itemList", itemList);
        modelMap.addAttribute("totalPrice", 0);
        return new ModelAndView(ORDER_PAGE, modelMap);
    }

    @PostMapping
    // TODO refactor
    public ModelAndView doPost(Long orderId,
                               ModelMap modelMap) {
        // TODO update user data
        Optional<Order> optionalOrder = orderService.readOrderById(orderId);
        if(optionalOrder.isPresent()){
            orderService.changeOrderStatus(optionalOrder, POST_MAPPING_ORDER_STATUS_STR);
            // TODO redirect to order page
        }
        return new ModelAndView("redirect:/" + HOME_PAGE, modelMap);
    }

}