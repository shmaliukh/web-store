package com.vshmaliukh.webstore.controllers.admin.item;

import com.vshmaliukh.webstore.ItemUtil;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.services.ItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/admin/item/view")
public class ViewItemController {

    final ItemService itemService;

    @GetMapping("/{itemType}")
    public ModelAndView doGet(@PathVariable("itemType") String itemType,
                              ModelMap modelMap) {
        List<? extends Item> itemList = itemService.readAllItemsByTypeName(itemType);
        if (itemList == null) {
            return new ModelAndView("redirect:/admin/item/view", modelMap);
        }
        modelMap.addAttribute("itemType", itemType);
        modelMap.addAttribute("itemList", itemList);
        return new ModelAndView("admin-item-view", modelMap);
    }

    @GetMapping
    public ModelAndView doGet(ModelMap modelMap) {
        List<Item> itemAllTypeList = new ArrayList<>();
        for (String itemTypeStr : ItemUtil.itemNameList) {
            List<? extends Item> itemListByType = itemService.readAllItemsByTypeName(itemTypeStr);
            if(itemListByType != null){
                itemAllTypeList.addAll(itemListByType);
            }
        }
        modelMap.addAttribute("itemType", "all");
        modelMap.addAttribute("itemList", itemAllTypeList);
        return new ModelAndView("admin-item-view", modelMap);
    }

}
