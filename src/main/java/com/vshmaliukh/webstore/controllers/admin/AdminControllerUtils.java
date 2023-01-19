package com.vshmaliukh.webstore.controllers.admin;

import com.vshmaliukh.webstore.controllers.utils.TableContentImp;
import com.vshmaliukh.webstore.model.Category;
import com.vshmaliukh.webstore.model.Order;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.CategoryRepository;
import com.vshmaliukh.webstore.repositories.OrderRepository;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.BaseItemRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.ModelMap;

import java.util.List;


public final class AdminControllerUtils {

    private AdminControllerUtils() {
    }

    public static void addAttributesForSortingAndPaging(int size, ModelMap modelMap, String sortField, String sortDirection, Page<?> pageWithItems) {
        modelMap.addAttribute("currentPage", pageWithItems.getNumber() + 1);
        modelMap.addAttribute("totalItems", pageWithItems.getTotalElements());
        modelMap.addAttribute("totalPages", pageWithItems.getTotalPages());
        modelMap.addAttribute("pageSize", size);
        modelMap.addAttribute("sortField", sortField);
        modelMap.addAttribute("sortDirection", sortDirection);
        modelMap.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
    }

    public static <T extends Item> List<T> getSortedItemsContent(String keyword, int page, int size, String sortField, String sortDirection, ModelMap modelMap, BaseItemRepository<T> repositoryByItemClassName) {
//      TODO refactor duplicate

        Sort.Direction direction = sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort.Order order = new Sort.Order(direction, sortField);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(order));

        Page<T> pageWithItems;
        if (StringUtils.isBlank(keyword)) {
            pageWithItems = repositoryByItemClassName.findAll(pageable);
        } else {
            pageWithItems = repositoryByItemClassName.findByNameContainingIgnoreCase(keyword, pageable);
            modelMap.addAttribute("keyword", keyword);
        }
        List<T> content = pageWithItems.getContent();
        addAttributesForSortingAndPaging(size, modelMap, sortField, sortDirection, pageWithItems);
        return content;
    }

    public static List<Order> getSortedOrderContent(String keyword, int page, int size, String sortField, String sortDirection, ModelMap modelMap, OrderRepository orderRepository) {

        Sort.Direction direction = sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort.Order order = new Sort.Order(direction, sortField);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(order));

        Page<Order> pageWithOrders;
        if (StringUtils.isBlank(keyword)) {
            pageWithOrders = orderRepository.findAll(pageable);
        } else {
            pageWithOrders = orderRepository.findByStatusContainingIgnoreCase(keyword, pageable);
            modelMap.addAttribute("keyword", keyword);
        }
        List<Order> content = pageWithOrders.getContent();

        addAttributesForSortingAndPaging(size, modelMap, sortField, sortDirection, pageWithOrders);

        return content;
    }

    public static List<Category> getSortedContent(String keyword,
                                                  int page,
                                                  int size,
                                                  String sortField, String sortDirection,
                                                  ModelMap modelMap,
                                                  CategoryRepository repository) {
        Sort.Direction direction = sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort.Order order = new Sort.Order(direction, sortField);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(order));

        Page<Category> pageWithContent;
        if (StringUtils.isBlank(keyword)) {
            pageWithContent = repository.findAll(pageable);
        } else {
//          FIXME refactor duplicate methods (solve problem with repository find by keyword pageable content)
            pageWithContent = repository.findByNameContainingIgnoreCase(keyword, pageable);
            modelMap.addAttribute("keyword", keyword);
        }
        List<Category> content = pageWithContent.getContent();

        addAttributesForSortingAndPaging(size, modelMap, sortField, sortDirection, pageWithContent);

        return content;
    }

    // TODO is necessary to refactor method ?
    // @author vshmaliukh
    // @since  2022-01-19
    public static TableContentImp<? extends Item> getTableContentForItemView(String keyword, int page, int size, String sortField, String sortDirection, BaseItemRepository itemRepository) {
        TableContentImp<? extends Item> tableContent = new TableContentImp(keyword, page, size, sortField, sortDirection) {
            @Override
            public Page<? extends Item> formPageIfKeywordIsBlank(Pageable pageable) {
                return itemRepository.findAll(pageable);
            }

            @Override
            public Page<? extends Item> formPageWithContentByKeyword(String keyword, Pageable pageable) {
                return itemRepository.findByNameContainingIgnoreCase(keyword, pageable);
            }
        };
        return tableContent;
    }

}
