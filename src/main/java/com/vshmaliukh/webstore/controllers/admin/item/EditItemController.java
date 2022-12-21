package com.vshmaliukh.webstore.controllers.admin.item;

import com.vshmaliukh.webstore.controllers.admin.AdminControllerUtils;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.ItemRepositoryProvider;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.ItemRepository;
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
@RequestMapping("/admin/item/edit")
public class EditItemController {

    final ItemService itemService;
    final ItemRepositoryProvider itemRepositoryProvider;

    @GetMapping("/{itemType}")
    public ModelAndView doGet(@RequestParam(required = false) String keyword,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "6") int size,
                              @RequestParam(defaultValue = "id,asc") String[] sort,
                              @PathVariable("itemType") String itemType,
                              ModelMap modelMap) {
        List itemList = itemService.readAllItemsByTypeName(itemType);
        if (itemList == null) {
            return new ModelAndView("redirect:/admin/item/edit", modelMap);
        }
        ItemRepository<? extends Item> repositoryByItemClassName = itemRepositoryProvider.getItemRepositoryByItemClassName(itemType);
        itemList = AdminControllerUtils.getSortedItemsContent(keyword, page, size, sort, modelMap, repositoryByItemClassName);

        modelMap.addAttribute("itemType", itemType.toLowerCase());
        modelMap.addAttribute("itemList", itemList);
        return new ModelAndView("admin-item-edit", modelMap);
    }

    @GetMapping("/**")
    public ModelAndView doGet(ModelMap modelMap) {
        return new ModelAndView("redirect:/admin/item", modelMap);
    }

}
