package com.vshmaliukh.webstore.controllers.admin;

import com.vshmaliukh.webstore.controllers.ConstantsForControllers;
import com.vshmaliukh.webstore.model.Category;
import com.vshmaliukh.webstore.model.items.Item;
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

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/admin/category")
@AllArgsConstructor
public class AdminCategoryController {

    final ItemService itemService;
    final ImageService imageService;
    final CategoryService categoryService;

    @GetMapping("/catalog")
    public ModelAndView doGetCatalog(@RequestParam(required = false) String keyword,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = ConstantsForControllers.DEFAULT_ITEM_QUANTITY_ON_PAGE) int size,
                                     @RequestParam(defaultValue = "id,asc") String[] sort,
                                     ModelMap modelMap) {
        List<Category> categoryList = AdminControllerUtils.getSortedContent(
                keyword, page, size, sort, modelMap, categoryService.getCategoryRepository()
        );
        modelMap.addAttribute("categoryList", categoryList);
        return new ModelAndView("admin/category/catalog", modelMap);
    }

    @GetMapping("/create")
    public ModelAndView doGetCreate(ModelMap modelMap) {
        return new ModelAndView("admin/category/create", modelMap);
    }

    @PostMapping("/save")
    public ModelAndView doPostSave(@RequestParam(name = "id", defaultValue = "") Integer id,
                                   @RequestParam(name = "name") String name,
                                   @RequestParam(name = "description") String description,
                                   @RequestParam(name = "isDeleted", defaultValue = "false") boolean isDeleted,
                                   @RequestParam(name = "isActivated", defaultValue = "false") boolean isActivated,
                                   ModelMap modelMap) {
        Category category = categoryService.getUpdatedOrCreateBaseCategory(id, name, description, isDeleted, isActivated);
        categoryService.save(category);
        return new ModelAndView("redirect:/admin/category/" + category.getId() + "/details", modelMap);
    }

    @PostMapping("/{categoryId}/image")
    public ModelAndView doPostSaveImage(@PathVariable(name = "categoryId") Integer categoryId,
                                        @RequestParam(name = "imageFile") MultipartFile imageFile,
                                        @RequestParam(name = "imageId", defaultValue = "null") Long imageId,
                                        ModelMap modelMap) {
        Optional<Category> optionalCategory = categoryService.readCategoryById(categoryId);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            categoryService.addImageToCategory(imageId, imageFile, category);
            return new ModelAndView("redirect:/admin/category/" + categoryId + "/details", modelMap);
        } else {
            log.warn("not found category entity by '{}' id to save imageFile: '{}'", categoryId, imageFile);
        }
        return new ModelAndView("redirect:/admin/category/catalog", modelMap);
    }

    @DeleteMapping("/{categoryId}/image")
    public ResponseEntity<Void> doDeleteImage(@PathVariable(name = "categoryId") Integer categoryId) {
        return categoryService.deleteImageByCategoryId(categoryId);
    }

    @GetMapping("/{categoryId}/details")
    public ModelAndView doGetDetails(@PathVariable(name = "categoryId") Integer categoryId,
                                     ModelMap modelMap) {
        Optional<Category> optionalCategory = categoryService.readCategoryById(categoryId);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();

            // TODO implement paging, sorting and find by keyword ability for 'items' tab ('details' template)
            // @author  vshmaliukh
            // @since   2022-01-19

            modelMap.addAttribute("category", category);
            return new ModelAndView("admin/category/details", modelMap);
        }
        return new ModelAndView("admin/category/catalog", modelMap);
    }

    @GetMapping("/{categoryId}/add-item")
    public ModelAndView doGetAddItem(@PathVariable(name = "categoryId") Integer categoryId,
                                     @RequestParam(required = false) String keyword,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = ConstantsForControllers.DEFAULT_ITEM_QUANTITY_ON_PAGE) int size,
                                     @RequestParam(defaultValue = "id,asc") String[] sort,
                                     @RequestParam(name = "itemType", defaultValue = "all") String itemType,
                                     ModelMap modelMap) {
        Optional<Category> optionalCategory = categoryService.readCategoryById(categoryId);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();

            BaseItemRepository itemRepository = itemService.getItemRepositoryByItemTypeName(itemType);
            List<? extends Item> itemList = AdminControllerUtils.getSortedItemsContent(keyword, page, size, sort, modelMap, itemRepository);

            modelMap.addAttribute("itemType", itemType.toLowerCase());
            modelMap.addAttribute("itemList", itemList);
            modelMap.addAttribute("category", category);
            return new ModelAndView("admin/category/add-item", modelMap);
        }
        return new ModelAndView("redirect:/admin/category/catalog", modelMap);
    }

    @PostMapping("/{categoryId}/add-item")
    public ModelAndView doPostAddItem(@PathVariable(name = "categoryId") Integer categoryId,
                                      @RequestParam(required = false) String keyword,
                                      @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = ConstantsForControllers.DEFAULT_ITEM_QUANTITY_ON_PAGE) int size,
                                      @RequestParam(defaultValue = "id,asc") String[] sort,
                                      @RequestParam("itemId") int itemId,
                                      @RequestParam(name = "itemType") String itemType,
                                      ModelMap modelMap) {
        Optional<Item> optionalItem = itemService.readItemById(itemId);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            if (item.getTypeStr().equalsIgnoreCase(itemType)) {
                Optional<Category> optionalCategory = categoryService.readCategoryById(categoryId);
                if (optionalCategory.isPresent()) {
                    Category category = optionalCategory.get();
                    categoryService.addItemToCategory(item, category);
                } else {
                    log.warn("problem to add item to category // item id: '{}', category id: '{}'" +
                            "// not found category", itemId, categoryId);
                }
            } else {
                log.warn("problem to add item to category // item id: '{}', category id: '{}'" +
                                "// request item type '{}' is not equals to found item '{}' type",
                        itemId, categoryId, itemType, item.getTypeStr());
            }
        } else {
            log.warn("problem to add item to category // item id: '{}', category id: '{}' " +
                    "// item not found", itemId, categoryId);
        }
        modelMap.addAttribute("keyword", keyword);
        modelMap.addAttribute("page", page);
        modelMap.addAttribute("size", size);
        modelMap.addAttribute("sort", sort);
        modelMap.addAttribute("itemType", itemType);
        return new ModelAndView("redirect:/admin/category/" + categoryId + "/add-item", modelMap);
    }

    @PostMapping("/{categoryId}/remove-item")
    public ModelAndView doPostRemoveItem(@PathVariable(name = "categoryId") Integer categoryId,
                                         @RequestParam(name = "itemId") Integer itemId,
                                         ModelMap modelMap) {
        Optional<Item> optionalItem = itemService.readItemById(itemId);
        Optional<Category> optionalCategory = categoryService.readCategoryById(categoryId);
        if (optionalCategory.isPresent() && optionalItem.isPresent()) {
            Item item = optionalItem.get();
            Category category = optionalCategory.get();
            categoryService.removeItemFromCategory(item, category);
        } else {
            log.warn("problem to remove item from category // item id: '{}', category id: '{}'", itemId, categoryId);
        }
        return new ModelAndView("redirect:/admin/category/" + categoryId + "/details", modelMap);
    }

}
