package com.vshmaliukh.webstore.controllers.admin;

import com.vshmaliukh.webstore.controllers.utils.TableContentImp;
import com.vshmaliukh.webstore.model.Category;
import com.vshmaliukh.webstore.model.Image;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.CategoryRepository;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.BaseItemRepository;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.ItemRepository;
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

import static com.vshmaliukh.webstore.controllers.admin.AdminControllerUtils.generateItemTableContentForCategoryDetails;
import static com.vshmaliukh.webstore.controllers.admin.AdminControllerUtils.generateTableContentForCategoryView;

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
                                     @RequestParam(defaultValue = "5") int size,
                                     @RequestParam(defaultValue = "id") String sortField,
                                     @RequestParam(defaultValue = "asc") String sortDirection,
                                     ModelMap modelMap) {
        CategoryRepository categoryRepository = categoryService.getCategoryRepository();
        TableContentImp<Category> tableContentForCategoryView
                = generateTableContentForCategoryView(keyword, page, size, sortField, sortDirection, categoryRepository);
        List<Category> categoryList = tableContentForCategoryView.readContentList();
        ModelMap contentModelMap = tableContentForCategoryView.readContentModelMap();

        modelMap.addAllAttributes(contentModelMap);
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
                                        @RequestParam(name = "imageId", required = false) Long imageId,
                                        ModelMap modelMap) {
        Optional<Category> optionalCategory = categoryService.readCategoryById(categoryId);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            Optional<Image> optionalImage = imageService.buildImageFromFile(imageFile);
            categoryService.addImageToCategory(imageId, optionalImage, category);
            return new ModelAndView("redirect:/admin/category/" + categoryId + "/details", modelMap);
        } else {
            log.warn("not found category entity by '{}' id to save imageFile: '{}'", categoryId, imageFile);
        }
        return new ModelAndView("redirect:/admin/category/catalog", modelMap);
    }

    @DeleteMapping("/{categoryId}/image")
    public ResponseEntity<Void> doDeleteImage(@PathVariable(name = "categoryId") Integer categoryId) {
        Optional<Category> optionalCategory = categoryService.readCategoryById(categoryId);
        return categoryService.deleteImageByCategory(optionalCategory);
    }

    @GetMapping("/{categoryId}/details")
    public ModelAndView doGetDetails(@PathVariable(name = "categoryId") Integer categoryId,
                                     @RequestParam(required = false) String keyword,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "5") int size,
                                     @RequestParam(defaultValue = "id") String sortField,
                                     @RequestParam(defaultValue = "asc") String sortDirection,
                                     ModelMap modelMap) {
        Optional<Category> optionalCategory = categoryService.readCategoryById(categoryId);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            ItemRepository allItemRepository = itemService.getItemRepository();
            TableContentImp<Item> itemTableContent
                    = generateItemTableContentForCategoryDetails(keyword, page, size, sortField, sortDirection, allItemRepository, category);
            List<? extends Item> itemList = itemTableContent.readContentList();
            ModelMap contentModelMap = itemTableContent.readContentModelMap();

            modelMap.addAllAttributes(contentModelMap);
            modelMap.addAttribute("itemList", itemList);
            modelMap.addAttribute("category", category);
            return new ModelAndView("admin/category/details", modelMap);
        }
        return new ModelAndView("admin/category/catalog", modelMap);
    }

    @GetMapping("/{categoryId}/add-item")
    public ModelAndView doGetAddItem(@PathVariable(name = "categoryId") Integer categoryId,
                                     @RequestParam(required = false) String keyword,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "5") int size,
                                     @RequestParam(defaultValue = "id") String sortField,
                                     @RequestParam(defaultValue = "asc") String sortDirection,
                                     @RequestParam(name = "itemType", defaultValue = "all") String itemType,
                                     ModelMap modelMap) {
        Optional<Category> optionalCategory = categoryService.readCategoryById(categoryId);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();

            BaseItemRepository itemRepository = itemService.getItemRepositoryByItemTypeName(itemType);
            AdminControllerUtils.addTableContentWithItems(keyword, page, size, sortField, sortDirection, itemType, modelMap, itemRepository);
            modelMap.addAttribute("itemType", itemType);
            modelMap.addAttribute("category", category);
            return new ModelAndView("admin/category/add-item", modelMap);
        }
        return new ModelAndView("redirect:/admin/category/catalog", modelMap);
    }

    @PostMapping("/{categoryId}/add-item")
    public ModelAndView doPostAddItem(@PathVariable(name = "categoryId") Integer categoryId,
                                      @RequestParam(required = false) String keyword,
                                      @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "5") int size,
                                      @RequestParam(defaultValue = "id") String sortField,
                                      @RequestParam(defaultValue = "asc") String sortDirection,
                                      @RequestParam("itemId") int itemId,
                                      @RequestParam("itemType") String itemType,
                                      ModelMap modelMap) {
        Optional<Item> optionalItem = itemService.readItemById(itemId);
        Optional<Category> optionalCategory = categoryService.readCategoryById(categoryId);
        if (optionalItem.isPresent() && optionalCategory.isPresent()) {
            Item item = optionalItem.get();
            Category category = optionalCategory.get();
            categoryService.addItemToCategory(item, category);

            modelMap.addAttribute("keyword", keyword);
            modelMap.addAttribute("page", page);
            modelMap.addAttribute("size", size);
            modelMap.addAttribute("sortField", sortField);
            modelMap.addAttribute("sortDirection", sortDirection);
            modelMap.addAttribute("itemType", itemType);
            return new ModelAndView("redirect:/admin/category/" + categoryId + "/add-item", modelMap);
        }
        log.warn("problem to add item to category // item id: '{}', category id: '{}' ", itemId, categoryId);
        return new ModelAndView("redirect:/admin/category/catalog", modelMap);
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

    @DeleteMapping("/{categoryId}")
    ResponseEntity<Void> doDelete(@PathVariable(name = "categoryId") Integer categoryId) {
        Optional<Category> optionalCategory = categoryService.readCategoryById(categoryId);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            categoryService.deleteCategory(category);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

}
