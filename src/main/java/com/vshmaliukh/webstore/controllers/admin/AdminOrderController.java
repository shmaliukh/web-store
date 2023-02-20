package com.vshmaliukh.webstore.controllers.admin;

import com.vshmaliukh.webstore.OrderStatus;
import com.vshmaliukh.webstore.controllers.utils.TableContentImp;
import com.vshmaliukh.webstore.model.Order;
import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.model.items.OrderItem;
import com.vshmaliukh.webstore.repositories.ItemRepositoryProvider;
import com.vshmaliukh.webstore.repositories.OrderItemRepository;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.ItemRepository;
import com.vshmaliukh.webstore.services.ItemService;
import com.vshmaliukh.webstore.services.OrderItemService;
import com.vshmaliukh.webstore.services.OrderService;
import com.vshmaliukh.webstore.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

import static com.vshmaliukh.webstore.controllers.admin.AdminControllerUtils.generateOrderItemTableContentForOrderDetails;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/admin/order")
public class AdminOrderController {

    private final UserService userService;
    private final ItemService itemService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final ItemRepositoryProvider itemRepositoryProvider;

    @GetMapping("/**")
    public ModelAndView doGet(ModelMap modelMap) {
        return new ModelAndView("redirect:/admin/order/catalog", modelMap);
    }

    @GetMapping("/catalog/**")
    public ModelAndView doGetCatalog(@RequestParam(required = false) String keyword,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "5") int size,
                                     @RequestParam(defaultValue = "id") String sortField,
                                     @RequestParam(defaultValue = "asc") String sortDirection,
                                     ModelMap modelMap) {
        TableContentImp<Order> tableContentForOrderView = AdminControllerUtils.generateTableContentForOrderView(keyword, page, size, sortField, sortDirection, orderService.getOrderRepository());
        List<Order> orderList = tableContentForOrderView.readContentList();
        ModelMap contentModelMap = tableContentForOrderView.readContentModelMap();

        modelMap.addAllAttributes(contentModelMap);
        modelMap.addAttribute("orderList", orderList);
        return new ModelAndView("/admin/order/catalog", modelMap);
    }

    @GetMapping("/view/{oderId}")
    public ModelAndView doGetView(@PathVariable(name = "oderId") Long id,
                                  ModelMap modelMap) {
        Optional<Order> optionalOrder = orderService.readOrderById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            List<OrderItem> orderItemList = orderService.readOrderItemListByOrderId(id);
            Integer totalOrderPrice = orderService.calcOrderTotalSum(order);

            modelMap.addAttribute("orderItemList", orderItemList);
            modelMap.addAttribute("order", order);
            modelMap.addAttribute("totalOrderPrice", totalOrderPrice);
            modelMap.addAttribute("orderStatusDescriptionMap", OrderStatus.getStatusNameDescriptionMap());
            return new ModelAndView("/admin/order/view", modelMap);
        }
        return new ModelAndView("redirect:/admin/order/catalog", modelMap);
    }

    @GetMapping("/edit/{oderId}")
    public ModelAndView doGetEdit(@PathVariable(name = "oderId") Long orderId,
                                  @RequestParam(required = false) String keyword,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam(defaultValue = "id") String sortField,
                                  @RequestParam(defaultValue = "asc") String sortDirection,
                                  ModelMap modelMap) {
        Optional<Order> optionalOrder = orderService.readOrderById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            OrderItemRepository orderItemRepository = orderItemService.getOrderItemRepository();
            TableContentImp<OrderItem> orderItemTableContent
                    = generateOrderItemTableContentForOrderDetails(keyword, page, size, sortField, sortDirection, orderItemRepository, order);
            List<OrderItem> orderItemList = orderItemTableContent.readContentList();
            ModelMap contentModelMap = orderItemTableContent.readContentModelMap();

            modelMap.addAllAttributes(contentModelMap);
            modelMap.addAttribute("order", order);
            modelMap.addAttribute("orderItemList", orderItemList);
            modelMap.addAttribute("orderStatusDescriptionMap", OrderStatus.getStatusNameDescriptionMap());
            return new ModelAndView("/admin/order/edit", modelMap);
        }
        return new ModelAndView("redirect:/admin/order/catalog", modelMap);
    }

    @PostMapping("/edit/{oderId}")
    public ModelAndView doPostEditOrder(@PathVariable(name = "oderId") Long orderId,
                                        @RequestParam(value = "status") String status,
                                        @RequestParam(value = "comment") String comment,
                                        @RequestParam(required = false) String keyword,
                                        @RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "5") int size,
                                        @RequestParam(defaultValue = "id") String sortField,
                                        @RequestParam(defaultValue = "asc") String sortDirection,
                                        ModelMap modelMap) {
        Optional<Order> optionalOrder = orderService.readOrderById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(status);
            order.setComment(comment);
            orderService.save(order);

            modelMap.addAttribute("keyword", keyword);
            modelMap.addAttribute("page", page);
            modelMap.addAttribute("size", size);
            modelMap.addAttribute("sortField", sortField);
            modelMap.addAttribute("sortDirection", sortDirection);
            return new ModelAndView("redirect:/admin/order/edit/" + orderId, modelMap);
        }
        return new ModelAndView("redirect:/admin/catalog", modelMap);
    }


    @PostMapping("/edit/{oderId}/order-item/{orderItemId}")
    public ModelAndView doPostEditItem(@PathVariable(name = "oderId") Long orderId,
                                       @PathVariable(value = "orderItemId") Long orderItemId,
                                       @RequestParam(value = "price") Integer price,
                                       @RequestParam(value = "quantity") Integer newOrderItemQuantity,
                                       @RequestParam(value = "active", defaultValue = "false") boolean active,
                                       ModelMap modelMap) {
        Optional<Order> optionalOrder = orderService.readOrderById(orderId);
        if (optionalOrder.isPresent()) {
            Optional<OrderItem> optionalOrderItem = orderService.readOrderItemById(orderItemId);
            if (optionalOrderItem.isPresent()) {
                OrderItem orderItem = optionalOrderItem.get();
                int oldOrderItemQuantity = orderItem.getQuantity();

                orderItem.setOrderItemPrice(price);
                orderItem.setActive(active);
                orderItem.setQuantity(newOrderItemQuantity);

                // TODO refactor
                orderService.setUpItemAvailableQuantity(orderItem, oldOrderItemQuantity);

                orderItemService.save(orderItem);
            }
        } else {
            return new ModelAndView("redirect:/admin/catalog", modelMap);
        }
        return new ModelAndView("redirect:/admin/order/edit/" + orderId, modelMap);
    }

    @GetMapping("/{oderId}/add-item")
    public ModelAndView doGetAddItem(@RequestParam(required = false) String keyword,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "5") int size,
                                     @RequestParam(defaultValue = "id") String sortField,
                                     @RequestParam(defaultValue = "asc") String sortDirection,
                                     @PathVariable(name = "oderId") Long orderId,
                                     ModelMap modelMap) {
        Optional<Order> optionalOrder = orderService.readOrderById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            ItemRepository allItemRepository = itemRepositoryProvider.getAllItemRepository();
            AdminControllerUtils.addTableContentWithItems(keyword, page, size, sortField, sortDirection, "all", modelMap, allItemRepository);
            Integer totalOrderItems = orderService.calcTotalOrderItemQuantity(order);

            modelMap.addAttribute("order", order);
            modelMap.addAttribute("totalOrderItems", totalOrderItems);
            return new ModelAndView("admin/order/add-item", modelMap);
        }
        return new ModelAndView("redirect:/admin/order/catalog", modelMap);
    }

    @PostMapping("/{oderId}/add-item")
    public ModelAndView doPostAddItem(@PathVariable(name = "oderId") Long orderId,
                                      @RequestParam(name = "itemId") Integer itemId,
                                      @RequestParam(name = "quantityToBuy") Integer quantityToBuy,
                                      ModelMap modelMap) {
        Optional<Order> optionalOrder = orderService.readOrderById(orderId);
        Optional<Item> optionalItem = itemService.readItemById(itemId);
        boolean isOptionalOrderPresent = optionalOrder.isPresent();
        boolean isOptionalItemPresent = optionalItem.isPresent();
        if (isOptionalOrderPresent && isOptionalItemPresent) {
            Order order = optionalOrder.get();
            Item item = optionalItem.get();
            orderService.insertItemToOrder(order, item, quantityToBuy);

            modelMap.addAttribute("order", order);
            return new ModelAndView("redirect:/admin/order/view/" + orderId, modelMap);
        }
        log.warn("problem to add item"
                + (!isOptionalOrderPresent ? " // not found order by id" : "")
                + (!isOptionalItemPresent ? " // not found item by id" : ""));
        return new ModelAndView("redirect:/admin/order/catalog", modelMap);
    }

    @GetMapping("/create")
    public ModelAndView doGetCreateOrder(@RequestParam(name = "userName", defaultValue = "") String userName,
                                         ModelMap modelMap) {
        List<User> userList = userService.readAllUserList();

        modelMap.addAttribute("userName", userName);
        modelMap.addAttribute("userList", userList);
        modelMap.addAttribute("orderStatusDescriptionMap", OrderStatus.getStatusNameDescriptionMap());
        return new ModelAndView("/admin/order/create", modelMap);
    }

    @PostMapping("/create")
    public ModelAndView doPostCreateOrder(@RequestParam(name = "userId") Long userId,
                                          @RequestParam(name = "status") String status,
                                          @RequestParam(name = "comment", defaultValue = "") String comment,
                                          ModelMap modelMap) {
        Optional<Order> optionalOrder = orderService.createEmptyOrder(userId, status, comment);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            orderService.save(order);
            return new ModelAndView("redirect:/admin/order/edit/" + order.getId(), modelMap);
        }
        return new ModelAndView("redirect:/admin/order/catalog/", modelMap);
    }

}

