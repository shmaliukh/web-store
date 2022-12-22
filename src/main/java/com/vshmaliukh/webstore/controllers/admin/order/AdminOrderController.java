package com.vshmaliukh.webstore.controllers.admin.order;

import com.vshmaliukh.webstore.controllers.admin.AdminControllerUtils;
import com.vshmaliukh.webstore.model.Order;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.services.ItemService;
import com.vshmaliukh.webstore.services.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/admin/order")
public class AdminOrderController {

    final OrderService orderService;
    final ItemService itemService;

    @GetMapping("/**")
    public ModelAndView doGet(ModelMap modelMap) {
        return new ModelAndView("redirect:/admin/order/catalog", modelMap);
    }

    @GetMapping("/catalog/**")
    public ModelAndView doGet(@RequestParam(required = false) String keyword,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "6") int size,
                              @RequestParam(defaultValue = "id,asc") String[] sort,
                              ModelMap modelMap) {
        List<Order> orderList = AdminControllerUtils.getSortedOrderContent(keyword, page, size, sort, modelMap, orderService.getOrderRepository());

        modelMap.addAttribute("orderList", orderList);
        return new ModelAndView("/admin/order/catalog", modelMap);
    }

    //    @GetMapping("/view/{id}")
//    public ModelAndView doGetInfo(@PathVariable(name = "id") Long id,
//                                  ModelMap modelMap) {
//
//        Order order = orderService.readOrderById(id);
//        if(order != null){
//            Long userId = order.getUserId();
//            Date dateCreated = order.getDateCreated();
//            Map<String, Item> typeItemMap = orderService.readOrderTypeItemMap(userId, dateCreated);
//            Map<String, List<Item>> typeItemListMap = new HashMap<>();
//            for (String itemType : typeItemMap.keySet()) {
//                List<Item> itemByTypeList = typeItemListMap.get(itemType);
//                typeItemListMap.put(itemType, itemByTypeList);
//            }
//
//            modelMap.addAttribute("typeItemListMap", typeItemListMap);
//            return new ModelAndView("view-test", modelMap);
//        }
//        return new ModelAndView("/admin/order/catalog", modelMap);
//    }
//
    @GetMapping("/view/{id}")
    public ModelAndView doGetInfo(@PathVariable(name = "id") Long id,
                                  ModelMap modelMap) {
        Order order = orderService.readOrderById(id);
        if (order != null) {
            List<Item> itemList;
            // TODO refactor to read all order items
            Integer itemId = order.getItemId();
            String itemClassType = order.getItemClassType();
            Item item = itemService.readItemByIdAndType(itemId, itemClassType);
            if (item != null) {
                itemList = Collections.singletonList(item);
            } else {
                itemList = Collections.emptyList();
            }

            modelMap.addAttribute("itemType", itemClassType.toLowerCase());
            modelMap.addAttribute("itemList", itemList);
            modelMap.addAttribute("order", order);
            return new ModelAndView("/admin/order/view", modelMap);
        }
        return new ModelAndView("redirect:/admin/order/catalog", modelMap);
    }

}
