package com.vshmaliukh.webstore.controllers.admin.item;

import com.vshmaliukh.webstore.controllers.ConstantsForControllers;
import com.vshmaliukh.webstore.controllers.admin.AdminControllerUtils;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.ItemRepositoryProvider;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.BaseItemRepository;
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
    final ItemRepositoryProvider itemRepositoryProvider;

    @GetMapping("/{itemType}")
    public ModelAndView doGet(@RequestParam(required = false) String keyword,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = ConstantsForControllers.DEFAULT_ITEM_QUANTITY_ON_PAGE) int size,
                              @RequestParam(defaultValue = "id,asc") String[] sort,
                              @PathVariable("itemType") String itemType,
                              ModelMap modelMap) {
        BaseItemRepository itemRepository = getItemRepositoryByItemType(itemType);
        List<? extends Item> itemList = AdminControllerUtils.getSortedItemsContent(keyword, page, size, sort, modelMap, itemRepository);

        modelMap.addAttribute("itemType", itemType.toLowerCase());
        modelMap.addAttribute("itemList", itemList);
        return new ModelAndView("/admin/item/view", modelMap);
    }

    private BaseItemRepository getItemRepositoryByItemType(String itemType) {
        // TODO solve 'Raw use of parameterized class 'BaseItemRepository''
        BaseItemRepository itemRepository = itemRepositoryProvider.getItemRepositoryByItemClassName(itemType);
        if(itemRepository != null){
            return itemRepository;
        }
        return itemRepositoryProvider.getAllItemRepository();
    }

}
