package com.vshmaliukh.webstore.controllers.admin.item;

import com.vshmaliukh.webstore.ItemUtil;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.services.ItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.ADMIN_ITEM_VIEW_PAGE;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("admin/item/view")
public class ViewItemController {

    ItemService itemService;

    @GetMapping("/{itemType}")
    public ModelAndView doGet(@PathVariable("itemType") String itemType,
                              ModelMap modelMap) {
        List<? extends Item> itemList;
        if(StringUtils.isNotBlank(itemType)){
            itemList = itemService.readAllItemsByTypeName(itemType);

        }else {
            for (String itemTypeStr : ItemUtil.itemNameList) {

            }
            itemList = itemService.readAllItemsByTypeName(itemType);
        }

        modelMap.addAttribute("itemList", itemList);
        return new ModelAndView(ADMIN_ITEM_VIEW_PAGE);
    }

}
