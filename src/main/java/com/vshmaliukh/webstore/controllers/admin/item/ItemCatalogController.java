package com.vshmaliukh.webstore.controllers.admin.item;

import com.vshmaliukh.webstore.ItemUtil;
import com.vshmaliukh.webstore.services.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.*;

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
        return new ModelAndView(ADMIN_ITEM_CATALOG_PAGE, modelMap);
    }

}
