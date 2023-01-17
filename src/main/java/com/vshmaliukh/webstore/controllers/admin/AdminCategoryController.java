package com.vshmaliukh.webstore.controllers.admin;

import com.vshmaliukh.webstore.controllers.ConstantsForControllers;
import com.vshmaliukh.webstore.model.Category;
import com.vshmaliukh.webstore.model.items.Item;
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
                                   @RequestParam(name = "isActivated", defaultValue = "true") boolean isActivated,
                                   ModelMap modelMap) {
        Category category = categoryService.buildBaseCategory(id, name, description, isDeleted, isActivated);
        categoryService.save(category);
        return new ModelAndView("redirect:/admin/category/" + category.getId() + "/details", modelMap);
    }

    @PostMapping("/{categoryId}/image")
    public ModelAndView doPostSaveImage(@PathVariable(name = "categoryId") Integer categoryId,
                                        @RequestParam(name = "imageFile") MultipartFile imageFile,
                                        ModelMap modelMap) {
        Optional<Category> optionalCategory = categoryService.readCategoryById(categoryId);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            categoryService.addImageToCategory(imageFile, category);
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
            List<Item> itemList = categoryService.readCategoryItemList(category);

            modelMap.addAttribute("category", category);
            modelMap.addAttribute("itemList", itemList);
            return new ModelAndView("admin/category/details", modelMap);
        }
        return new ModelAndView("admin/category/catalog", modelMap);
    }

    @PostMapping("category/add-item")
    public ModelAndView doPostCreateItemByCategory(@RequestParam(name = "itemType") String itemType,
                                                   ModelMap modelMap) {
        if (itemService.isTypeExists(itemType)) {
            return new ModelAndView("redirect:/admin/item/add/" + itemType, modelMap);
        }
        return new ModelAndView("redirect:/admin/category/catalog", modelMap);
    }

}
