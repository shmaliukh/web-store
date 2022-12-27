package com.vshmaliukh.webstore.controllers.admin.order;

import com.vshmaliukh.webstore.controllers.ConstantsForControllers;
import com.vshmaliukh.webstore.controllers.admin.AdminControllerUtils;
import com.vshmaliukh.webstore.model.Order;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.model.items.OrderItem;
import com.vshmaliukh.webstore.repositories.ItemRepositoryProvider;
import com.vshmaliukh.webstore.services.ItemService;
import com.vshmaliukh.webstore.services.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/admin/order")
public class AdminOrderController {

    final ItemRepositoryProvider itemRepositoryProvider;
    final ItemService itemService;
    final OrderService orderService;

    @GetMapping("/**")
    public ModelAndView doGet(ModelMap modelMap) {
        return new ModelAndView("redirect:/admin/order/catalog", modelMap);
    }

    @GetMapping("/catalog/**")
    public ModelAndView doGetCatalog(@RequestParam(required = false) String keyword,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "6") int size,
                                     @RequestParam(defaultValue = "id,asc") String[] sort,
                                     ModelMap modelMap) {
        List<Order> orderList = AdminControllerUtils.getSortedOrderContent(keyword, page, size, sort, modelMap, orderService.getOrderRepository());

        modelMap.addAttribute("orderList", orderList);
        return new ModelAndView("/admin/order/catalog", modelMap);
    }

    @GetMapping("/view/{oderId}")
    public ModelAndView doGetView(@PathVariable(name = "oderId") Long id,
                                  ModelMap modelMap) {
        Order order = orderService.readOrderById(id);
        if (order != null) {
            List<OrderItem> orderItemList = orderService.readOrderItemListByOrderId(id);
            Integer totalOrderPrice = orderService.calcTotalOrderPrice(order);

            modelMap.addAttribute("orderItemList", orderItemList);
            modelMap.addAttribute("order", order);
            modelMap.addAttribute("totalOrderPrice", totalOrderPrice);
            modelMap.addAttribute("orderStatusDescriptionMap", ConstantsForControllers.orderStatusDescriptionMap);
            return new ModelAndView("/admin/order/view", modelMap);
        }
        return new ModelAndView("redirect:/admin/order/catalog", modelMap);
    }

    @GetMapping("/edit/{oderId}")
    public ModelAndView doGetEdit(@PathVariable(name = "oderId") Long orderId,
                                  ModelMap modelMap) {
        Order order = orderService.readOrderById(orderId);
        if (order != null) {
            List<OrderItem> orderItemList = orderService.readOrderItemListByOrderId(orderId);

            modelMap.addAttribute("orderItemList", orderItemList);
            modelMap.addAttribute("order", order);
            modelMap.addAttribute("orderStatusDescriptionMap", ConstantsForControllers.orderStatusDescriptionMap);
            return new ModelAndView("/admin/order/edit", modelMap);
        }
        return new ModelAndView("redirect:/admin/order/catalog", modelMap);
    }

    @PostMapping("/edit/{oderId}")
    public ModelAndView doPostEditOrder(@PathVariable(name = "oderId") Long orderId,
                                        @RequestParam(value = "status") String status,
                                        @RequestParam(value = "comment") String comment,
                                        ModelMap modelMap) {
        Order order = orderService.readOrderById(orderId);
        if (order != null) {
            order.setStatus(status);
            order.setComment(comment);
            orderService.saveOrder(order);
        } else {
            return new ModelAndView("redirect:/admin/catalog", modelMap);
        }
        return new ModelAndView("redirect:/admin/order/edit/" + orderId, modelMap);
    }


    @PostMapping("/edit/{oderId}/order-item/{orderItemId}")
    public ModelAndView doPostEditItem(@PathVariable(name = "oderId") Long orderId,
                                       @PathVariable(value = "orderItemId") Long orderItemId,
                                       @RequestParam(value = "price") Integer price,
                                       @RequestParam(value = "quantity") Integer quantity,
                                       @RequestParam(value = "active", defaultValue = "false") boolean active,
                                       ModelMap modelMap) {
        Order order = orderService.readOrderById(orderId);
        if (order != null) {
            OrderItem orderItem = orderService.readOrderItemById(orderItemId);
            if (orderItem != null) {
                orderItem.setOrderItemPrice(price);
                orderItem.setQuantity(quantity);
                orderItem.setActive(active);
                orderService.saveOrderItem(orderItem);
            }
        } else {
            return new ModelAndView("redirect:/admin/catalog", modelMap);
        }
        return new ModelAndView("redirect:/admin/order/edit/" + orderId, modelMap);
    }

    @GetMapping("/{oderId}/add-item")
    public ModelAndView doGetAddItem(@RequestParam(required = false) String keyword,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "6") int size,
                                     @RequestParam(defaultValue = "id,asc") String[] sort,
                                     @PathVariable(name = "oderId") Long orderId,
                                     ModelMap modelMap) {
        Order order = orderService.readOrderById(orderId);
        if (order != null) {
            List<Item> itemsAvailableToBuy = AdminControllerUtils.getSortedItemsContent(keyword, page, size, sort, modelMap, itemRepositoryProvider.getAllItemRepository());
            Integer totalOrderItems = orderService.calcTotalOrderItems(order);
//            List<Item> itemsAvailableToBuy = itemService.readItemsAvailableToBuy();

            modelMap.addAttribute("order", order);
            modelMap.addAttribute("totalOrderItems", totalOrderItems);
            modelMap.addAttribute("itemList", itemsAvailableToBuy);
            return new ModelAndView("admin/order/add-item", modelMap);
        }
        return new ModelAndView("redirect:/admin/order/catalog", modelMap);
    }

    @PostMapping("/{oderId}/add-item")
    public ModelAndView doPostAddItem(@PathVariable(name = "oderId") Long orderId,
                                      @RequestParam(name = "itemId") Integer itemId,
                                      @RequestParam(name = "quantityToBuy") Integer quantityToBuy,
                                      ModelMap modelMap) {
        Order order = orderService.readOrderById(orderId);
        if (order != null) {
            orderService.insertItemToOrder(orderId, itemId, quantityToBuy);

            modelMap.addAttribute("order", order);
            return new ModelAndView("redirect:/admin/order/view/" + orderId, modelMap);
        }
        return new ModelAndView("redirect:/admin/order/catalog", modelMap);
    }

}

