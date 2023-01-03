package com.vshmaliukh.webstore.controllers.admin.item;

import com.vshmaliukh.webstore.ItemUtil;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.services.ItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.*;


@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/admin/item")
public class ItemCatalogController {

    final ItemService itemService;

    @GetMapping("/**")
    public ModelAndView doRedirectToCatalog(ModelMap modelMap) {
        return new ModelAndView("redirect:/admin/item/catalog", modelMap);
    }

    @GetMapping("/catalog")
    public ModelAndView doGetCatalog(ModelMap modelMap) {
        Map<String, Integer> itemTypeQuantityMap = new HashMap<>();
        List<String> itemTypeList = ItemUtil.itemNameList;

        itemTypeList.forEach(o -> itemTypeQuantityMap.put(o, itemService.readAllItemsByTypeName(o).size()));
        modelMap.addAttribute("itemTypeQuantityMap", itemTypeQuantityMap);
        return new ModelAndView("/admin/item/catalog", modelMap);
    }

    @GetMapping("/details/{itemId}")
    public ModelAndView doGetDetails(@PathVariable(name = "itemId") Integer itemId,
                                     ModelMap modelMap) {
        Optional<Item> optionalItem = itemService.readItemById(itemId);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();

            modelMap.addAttribute("item", item);
            return new ModelAndView("/admin/item/details", modelMap);
        }
        log.warn("problem to generate '/item/details/{}' template // not found item with '{}' id", itemId, itemId);
        return new ModelAndView("redirect:/admin/item/catalog", modelMap);
    }

}
