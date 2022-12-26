package com.vshmaliukh.webstore.controllers.admin;

import com.vshmaliukh.webstore.model.Order;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.OrderRepository;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.BaseItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.ModelMap;

import java.util.List;


public final class AdminControllerUtils {

    private AdminControllerUtils(){}

    public static void addAttributesForSortingAndPaging(int size, ModelMap modelMap, String sortField, String sortDirection, Page<?> pageWithItems) {
        modelMap.addAttribute("currentPage", pageWithItems.getNumber() + 1);
        modelMap.addAttribute("totalItems", pageWithItems.getTotalElements());
        modelMap.addAttribute("totalPages", pageWithItems.getTotalPages());
        modelMap.addAttribute("pageSize", size);
        modelMap.addAttribute("sortField", sortField);
        modelMap.addAttribute("sortDirection", sortDirection);
        modelMap.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
    }

    public static <T extends Item>  List<T> getSortedItemsContent(String keyword, int page, int size, String[] sort, ModelMap modelMap, BaseItemRepository<T> repositoryByItemClassName) {
//      TODO refactor duplicate
        String sortField = sort[0];
        String sortDirection = sort[1];

        Sort.Direction direction = sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort.Order order = new Sort.Order(direction, sortField);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(order));

        Page<T> pageWithItems;
        if (keyword == null) {
            pageWithItems = repositoryByItemClassName.findAll(pageable);
        } else {
            pageWithItems = repositoryByItemClassName.findByNameContainingIgnoreCase(keyword, pageable);
            modelMap.addAttribute("keyword", keyword);
        }
        List<T> content = pageWithItems.getContent();
        addAttributesForSortingAndPaging(size, modelMap, sortField, sortDirection, pageWithItems);
        return content;
    }

    public static List<Order> getSortedOrderContent(String keyword, int page, int size, String[] sort, ModelMap modelMap, OrderRepository orderRepository) {
        String sortField = sort[0];
        String sortDirection = sort[1];

        Sort.Direction direction = sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort.Order order = new Sort.Order(direction, sortField);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(order));

        Page<Order> pageWithOrders;
        if (keyword == null) {
            pageWithOrders = orderRepository.findAll(pageable);
        } else {
            pageWithOrders = orderRepository.findByStatusIgnoreCase(keyword, pageable);
            modelMap.addAttribute("keyword", keyword);
        }
        List<Order> content = pageWithOrders.getContent();

        addAttributesForSortingAndPaging(size, modelMap, sortField, sortDirection, pageWithOrders);

        return content;
    }

}
