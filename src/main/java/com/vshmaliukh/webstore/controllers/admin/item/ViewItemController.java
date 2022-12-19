package com.vshmaliukh.webstore.controllers.admin.item;

import com.vshmaliukh.webstore.ItemUtil;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.ActionsWithItemRepositoryProvider;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.ActionsWithItem;
import com.vshmaliukh.webstore.services.ItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/admin/item/view")
public class ViewItemController {

    final ItemService itemService;
    final ActionsWithItemRepositoryProvider actionsWithItemRepositoryProvider;


    @GetMapping("/{itemType}")
    public ModelAndView doGet(@RequestParam(required = false) String keyword,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "6") int size,
                              @RequestParam(defaultValue = "id,asc") String[] sort,
                              @PathVariable("itemType") String itemType,
                              ModelMap modelMap) {
        List<? extends Item> itemList = itemService.readAllItemsByTypeName(itemType);
        if (itemList == null) {
            return new ModelAndView("redirect:/admin/item/view", modelMap);
        }
        String sortField = sort[0];
        String sortDirection = sort[1];

        Sort.Direction direction = sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort.Order order = new Sort.Order(direction, sortField);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(order));

        Page<? extends Item> pageWithItems;
        ActionsWithItem<? extends Item> repositoryByItemClassName = actionsWithItemRepositoryProvider.getActionsWithItemRepositoryByItemClassName(itemType);
        if (keyword == null) {
            pageWithItems = repositoryByItemClassName.findAll(pageable);
        } else {
            pageWithItems = repositoryByItemClassName.findByNameContainingIgnoreCase(keyword, pageable);
            modelMap.addAttribute("keyword", keyword);
        }
        itemList = pageWithItems.getContent();

        modelMap.addAttribute("currentPage", pageWithItems.getNumber() + 1);
        modelMap.addAttribute("totalItems", pageWithItems.getTotalElements());
        modelMap.addAttribute("totalPages", pageWithItems.getTotalPages());
        modelMap.addAttribute("pageSize", size);
        modelMap.addAttribute("sortField", sortField);
        modelMap.addAttribute("sortDirection", sortDirection);
        modelMap.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");

        modelMap.addAttribute("itemType", itemType.toLowerCase());
        modelMap.addAttribute("itemList", itemList);
        return new ModelAndView("admin-item-view", modelMap);
    }

    @GetMapping("/**")
    public ModelAndView doGet(ModelMap modelMap) {
        List<Item> itemAllTypeList = ItemUtil.readAllItems(itemService);
        modelMap.addAttribute("itemType", "all");
        modelMap.addAttribute("itemList", itemAllTypeList);
        return new ModelAndView("admin-item-view", modelMap);
    }

}
