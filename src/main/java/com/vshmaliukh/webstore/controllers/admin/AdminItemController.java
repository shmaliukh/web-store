package com.vshmaliukh.webstore.controllers.admin;

import com.vshmaliukh.webstore.ItemUtil;
import com.vshmaliukh.webstore.controllers.ConstantsForControllers;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.ItemRepositoryProvider;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.BaseItemRepository;
import com.vshmaliukh.webstore.services.ItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/admin/item")
public class AdminItemController {

    final ItemService itemService;
    final ItemRepositoryProvider itemRepositoryProvider;


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

    @GetMapping("/view/{itemType}")
    public ModelAndView doGetView(@RequestParam(required = false) String keyword,
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
        if (itemRepository != null) {
            return itemRepository;
        }
        return itemRepositoryProvider.getAllItemRepository();
    }

    @DeleteMapping("/delete")
    public <T extends Item> ResponseEntity<Void> doDelete(@RequestBody T item) {
        ResponseEntity<Void> responseEntity = getDeleteItemResponse(item);
        if (responseEntity != null) {
            return responseEntity;
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/delete/{itemId}")
    public ResponseEntity<Void> doDeleteById(@PathVariable Integer itemId) {
        Optional<Item> optionalItem = itemService.readItemById(itemId);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            ResponseEntity<Void> responseEntity = getDeleteItemResponse(item);
            if (responseEntity != null) return responseEntity;
            log.warn(" item '{}' not deleted from database", item);
        }
        return ResponseEntity.badRequest().build();
    }

    private ResponseEntity<Void> getDeleteItemResponse(Item item) {
        itemService.deleteItem(item);
        if (!itemService.isItemSaved(item)) {
            log.info("deleted item from database: '{}'", item);
            return ResponseEntity.ok().build();
        }
        return null;
    }

    @GetMapping("/add/{itemType}")
    public ModelAndView doGet(@PathVariable("itemType") String itemType,
                              ModelMap modelMap) {
        boolean itemTypeExist = ItemUtil.itemNameList.stream().anyMatch(itemType::equalsIgnoreCase);
        if (itemTypeExist) {
            modelMap.addAttribute(itemType, itemType.toLowerCase());
            return new ModelAndView("/admin/item/add", modelMap);
        }
        return new ModelAndView("redirect:/admin", modelMap);
    }

    @PostMapping("/add")
    public <T extends Item> ModelAndView doPost(@CookieValue(defaultValue = "0") Long userId,
                                                @RequestBody T item,
                                                ModelMap modelMap) {
        addItem(userId, item);
        return new ModelAndView("redirect:/home", modelMap);
    }

    @PutMapping("/add")
    <T extends Item> ResponseEntity<Void> addItem(@CookieValue(defaultValue = "0") Long userId,
                                                  @RequestBody T item) {
        itemService.saveItem(item);
        if (itemService.isItemSaved(item)) {
            log.info("saved item to database: '{}'", item);
            return ResponseEntity.ok().build();
        }
        log.warn("userId: '{}' // item '{}' not added to database", userId, item);
        return ResponseEntity.badRequest().build();
    }

}
