package com.vshmaliukh.webstore.controllers.admin.item;

import com.vshmaliukh.webstore.ItemUtil;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.services.ItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/admin/item/delete")
public class DeleteItemController {

    final ItemService itemService;

    @GetMapping("/{itemType}")
    public ModelAndView doGet(@PathVariable("itemType") String itemType,
                              ModelMap modelMap) {
        List<? extends Item> itemList = itemService.readAllItemsByTypeName(itemType);
        if (itemList == null) {
            return new ModelAndView("redirect:/admin/item/delete", modelMap);
        }
        modelMap.addAttribute("itemType", itemType.toLowerCase());
        modelMap.addAttribute("itemList", itemList);
        return new ModelAndView("admin-item-delete", modelMap);
    }

    @GetMapping("/**")
    public ModelAndView doGet(ModelMap modelMap) {
        return new ModelAndView("redirect:/admin/item", modelMap);
    }

    @DeleteMapping
    public <T extends Item> ResponseEntity<Void> doDelete(@RequestBody T item) {
        itemService.deleteItem(item);
        if (!itemService.isItemSaved(item)) {
            log.info("deleted item from database: '{}'", item);
            return ResponseEntity.ok().build();
        }
        log.warn(" item '{}' not deleted from database", item);
        return ResponseEntity.badRequest().build();
    }

}
