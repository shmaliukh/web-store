package com.vshmaliukh.webstore.controllers.admin;

import com.vshmaliukh.webstore.ItemUtil;
import com.vshmaliukh.webstore.controllers.utils.TableContentImp;
import com.vshmaliukh.webstore.model.Image;
import com.vshmaliukh.webstore.model.ItemImage;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.ItemRepositoryProvider;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.BaseItemRepository;
import com.vshmaliukh.webstore.services.CategoryService;
import com.vshmaliukh.webstore.services.ImageService;
import com.vshmaliukh.webstore.services.ItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/admin/item")
public class AdminItemController {

    public static final String NAV_MAIN_STR = "nav-main";
    public static final String NAV_IMAGES_STR = "nav-images";

    final ItemService itemService;
    final ImageService imageService;
    final CategoryService categoryService;
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

    @GetMapping("/{itemId}/details")
    public ModelAndView doGetDetails(@PathVariable(name = "itemId") Integer itemId,
                                     @RequestParam(defaultValue = NAV_MAIN_STR) String tab,
                                     ModelMap modelMap) {
        Optional<Item> optionalItem = itemService.readItemById(itemId);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            List<ItemImage> itemImageList = imageService.readImageListByItem(item);

            modelMap.addAttribute("item", item);
            modelMap.addAttribute("itemImageList", itemImageList);
            modelMap.addAttribute("tab", tab);
            return new ModelAndView("/admin/item/details", modelMap);
        }
        log.warn("problem to generate '/item/{}/details' template // not found item with '{}' id", itemId, itemId);
        return new ModelAndView("redirect:/admin/item/catalog", modelMap);
    }

    @GetMapping("/view/{itemType}")
    public ModelAndView doGetView(@RequestParam(required = false) String keyword,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam(defaultValue = "id") String sortField,
                                  @RequestParam(defaultValue = "asc") String sortDirection,
                                  @PathVariable("itemType") String itemType,
                                  ModelMap modelMap) {
        BaseItemRepository itemRepository = itemService.getItemRepositoryByItemTypeName(itemType);

        TableContentImp<? extends Item> tableContent = AdminControllerUtils.generateTableContentForItemView(keyword, page, size, sortField, sortDirection, itemRepository);
        List<? extends Item> itemList = tableContent.readContentList();
        ModelMap contentModelMap = tableContent.readContentModelMap();

        modelMap.addAllAttributes(contentModelMap);
        modelMap.addAttribute("itemType", itemType.toLowerCase());
        modelMap.addAttribute("itemList", itemList);
        return new ModelAndView("/admin/item/view", modelMap);
    }

    @DeleteMapping("/delete")
    public <T extends Item> ResponseEntity<Void> doDelete(@RequestBody T item) {
        ResponseEntity<Void> responseEntity = getDeleteItemResponse(item);
        if (responseEntity != null) {
            return responseEntity;
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{itemId}/delete")
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
                              @RequestParam(value = "categoryName", required = false) String categoryName,
                              ModelMap modelMap) {
        boolean itemTypeExist = ItemUtil.itemNameList.stream().anyMatch(itemType::equalsIgnoreCase);
        if (itemTypeExist) {
            List<String> statusNameList = itemService.readStatusNameList();
            List<String> categoryNameList = categoryService.readCategoryNameList();

            modelMap.addAttribute("categoryName", categoryName);
            modelMap.addAttribute("itemType", itemType.toLowerCase());
            modelMap.addAttribute("statusList", statusNameList);
            modelMap.addAttribute("categoryNameList", categoryNameList);
            return new ModelAndView("admin/item/create", modelMap);
        }
        return new ModelAndView("redirect:/admin", modelMap);
    }

    @PostMapping("/add")
    public <T extends Item> ModelAndView doPostAddItem(@CookieValue(defaultValue = "0") Long userId,
                                                       @RequestBody T item,
                                                       ModelMap modelMap) {
        doPutAddItem(userId, item);
        return new ModelAndView("redirect:/home", modelMap);
    }

    @PutMapping("/add")
    public <T extends Item> ResponseEntity<Void> doPutAddItem(@CookieValue(defaultValue = "0") Long userId,
                                                              @RequestBody T item) {
        itemService.saveItem(item);
        if (itemService.isItemSaved(item)) {
            log.info("saved item to database: '{}'", item);
            return ResponseEntity.ok().build();
        }
        log.warn("userId: '{}' // item '{}' not added to database", userId, item);
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/{itemId}/image")
    public ModelAndView uploadItemImage(@PathVariable Integer itemId,
                                        @RequestParam("imageFile") MultipartFile imageFile,
                                        ModelMap modelMap) {
        Optional<Item> optionalItem = itemService.readItemById(itemId);
        if (!imageFile.isEmpty() && optionalItem.isPresent()) {
            Item item = optionalItem.get();
            itemService.addImageToItem(item, imageFile);
        } else {
            log.warn("problem to upload image"
                    + (imageFile.isEmpty() ? " // imageFile is empty" : " // item is not present"));
        }
        modelMap.addAttribute("tab", NAV_IMAGES_STR);
        return new ModelAndView("redirect:/admin/item/{itemId}/details", modelMap);
    }

    @PostMapping("/{itemId}/image/{imageId}")
    public ModelAndView uploadItemImage(@PathVariable Integer itemId,
                                        @PathVariable Long imageId,
                                        @RequestParam("imageFile") MultipartFile imageFile,
                                        ModelMap modelMap) {
        Optional<Item> optionalItem = itemService.readItemById(itemId);
        Optional<ItemImage> optionalImage = imageService.readItemImageById(imageId);
        if (optionalItem.isPresent() && optionalImage.isPresent() && !imageFile.isEmpty()) {
            Item item = optionalItem.get();
            ItemImage itemImage = optionalImage.get();
            itemService.changeItemImage(item, itemImage, imageFile);
        } else {
            log.warn("problem to change item image"
                    + (!optionalItem.isPresent() ? " // item is not present" : "")
                    + (!optionalImage.isPresent() ? " // image is not present" : "")
                    + (imageFile.isEmpty() ? " // image file is empty" : ""));
        }
        modelMap.addAttribute("tab", NAV_IMAGES_STR);
        return new ModelAndView("redirect:/admin/item/{itemId}/details", modelMap);
    }

    @GetMapping("/{itemType}")
    public ResponseEntity<List<? extends Item>> readItemListByType(@PathVariable(name = "itemType") String itemType) {
        List<? extends Item> itemList = itemService.readAllItemsByTypeName(itemType);
        return ResponseEntity.ok().body(itemList);
    }

}
