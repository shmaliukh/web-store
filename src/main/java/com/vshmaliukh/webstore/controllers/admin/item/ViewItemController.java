package com.vshmaliukh.webstore.controllers.admin.item;

import com.vshmaliukh.webstore.ItemUtil;
import com.vshmaliukh.webstore.controllers.admin.AdminControllerUtils;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.ActionsWithItemRepositoryProvider;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.ActionsWithItem;
import com.vshmaliukh.webstore.services.ItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
        ActionsWithItem<? extends Item> repositoryByItemClassName = actionsWithItemRepositoryProvider.getActionsWithItemRepositoryByItemClassName(itemType);
        itemList = AdminControllerUtils.getSortedItemsContent(keyword, page, size, sort, modelMap, repositoryByItemClassName);

        modelMap.addAttribute("itemType", itemType.toLowerCase());
        modelMap.addAttribute("itemList", itemList);
        return new ModelAndView("admin-item-view", modelMap);
    }

    @GetMapping(value = {"/all"})
    public ModelAndView doGet(@RequestParam(required = false) String keyword,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "6") int size,
                              @RequestParam(defaultValue = "id,asc") String[] sort,
                              ModelMap modelMap) {

        List<Item> itemAllTypeList = ItemUtil.readAllItems(itemService);
        for (ActionsWithItem<? extends Item> actionsWithItem : actionsWithItemRepositoryProvider.itemActionsWithRepositoryList) {
            itemAllTypeList.addAll(AdminControllerUtils.getSortedItemsContent(keyword, page, size, sort, modelMap, actionsWithItem));
        }

        modelMap.addAttribute("itemType", "all");
        modelMap.addAttribute("itemList", itemAllTypeList);
        return new ModelAndView("admin-item-view", modelMap);
    }

}
