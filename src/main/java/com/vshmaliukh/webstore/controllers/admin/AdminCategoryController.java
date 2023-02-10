package com.vshmaliukh.webstore.controllers.admin;

import com.vshmaliukh.webstore.controllers.utils.TableContentImp;
import com.vshmaliukh.webstore.dto.CategoryDto;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

import static com.vshmaliukh.webstore.controllers.admin.AdminControllerUtils.generateItemTableContentForCategoryDetails;
import static com.vshmaliukh.webstore.controllers.admin.AdminControllerUtils.generateTableContentForCategoryView;

@Slf4j
@Controller
@RequestMapping("/admin/category")
@AllArgsConstructor
public class AdminCategoryController {

    public static final String NAV_MAIN_STR = "nav-main";
    public static final String NAV_ITEMS_STR = "nav-items";
    public static final String NAV_IMAGE_STR = "nav-image";

    final ItemService itemService;
    final ImageService imageService;
    final CategoryService categoryService;

    @GetMapping("/**")
    public ModelAndView doGetAll(ModelMap modelMap) {
        return new ModelAndView("redirect:/catalog", modelMap);
    }

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

//    @PostMapping("/save")
//    public ModelAndView doPostSave(@RequestBody @Valid CategoryDto categoryDto,
//                                   ModelMap modelMap) {
//        Category category = categoryService.getUpdatedOrCreateBaseCategory(categoryDto);
//        categoryService.save(category);
//        modelMap.addAttribute("tab", NAV_MAIN_STR);
//        return new ModelAndView("redirect:/admin/category/" + category.getId() + "/details", modelMap);
//    }

    @PostMapping("/save")
    public ResponseEntity<CategoryDto> doPostSaveDto(@RequestBody @Valid CategoryDto categoryDto) {
        Category category = categoryService.getUpdatedOrCreateBaseCategory(categoryDto);
        categoryService.save(category);
        String categoryDtoName = categoryDto.getName();
        if (categoryService.isExistCategoryByName(categoryDtoName)) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new CategoryDto(category));
        } else {
            log.warn("problem to save category");
            return ResponseEntity.badRequest()
                    .build();
        }
    }

    @PostMapping("/{categoryId}/image")
    public ModelAndView doPostSaveImage(@PathVariable(name = "categoryId") @Min(1) Integer categoryId,
                                        @RequestParam(name = "imageFile") MultipartFile imageFile,
                                        @RequestParam(name = "imageId", required = false) Long imageId,
                                        ModelMap modelMap) {
        Optional<Category> optionalCategory = categoryService.readCategoryById(categoryId);
        modelMap.addAttribute("tab", NAV_IMAGE_STR);
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
    public ResponseEntity<Void> doDeleteImage(@PathVariable(name = "categoryId") @Min(1) Integer categoryId) {
        Optional<Category> optionalCategory = categoryService.readCategoryById(categoryId);
        return categoryService.deleteImageByCategory(optionalCategory);
    }

    @GetMapping("/{categoryId}/details")
    public ModelAndView doGetDetails(@PathVariable(name = "categoryId") @Min(1) Integer categoryId,
                                     @RequestParam(required = false) String keyword,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "5") int size,
                                     @RequestParam(defaultValue = "id") String sortField,
                                     @RequestParam(defaultValue = "asc") String sortDirection,
                                     @RequestParam(defaultValue = "", name = "tab") String tabRequestParam,
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
            modelMap.addAttribute("tab", readTabAttributeValue(tabRequestParam, modelMap));
            return new ModelAndView("admin/category/details", modelMap);
        }
        return new ModelAndView("admin/category/catalog", modelMap);
    }

    private static String readTabAttributeValue(String tabRequestParam, ModelMap modelMap) {
        String mapAttribute = (String) modelMap.getAttribute("tab");
        return StringUtils.isNotBlank(mapAttribute)
                ? mapAttribute
                : StringUtils.isNotBlank(tabRequestParam) ? tabRequestParam : NAV_MAIN_STR;
    }

    @GetMapping("/{categoryId}/add-item")
    public ModelAndView doGetAddItem(@PathVariable(name = "categoryId") @Min(1) Integer categoryId,
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

            BaseItemRepository<? extends Item> itemRepository = itemService.getItemRepositoryByItemTypeName(itemType);
            AdminControllerUtils.addTableContentWithItems(keyword, page, size, sortField, sortDirection, itemType, modelMap, itemRepository);
            modelMap.addAttribute("itemType", itemType);
            modelMap.addAttribute("category", category);
            return new ModelAndView("admin/category/add-item", modelMap);
        }
        return new ModelAndView("redirect:/admin/category/catalog", modelMap);
    }

    @PostMapping("/{categoryId}/add-item")
    public ModelAndView doPostAddItem(@PathVariable(name = "categoryId") @Min(1) Integer categoryId,
                                      @RequestParam(required = false) String keyword,
                                      @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "5") int size,
                                      @RequestParam(defaultValue = "id") String sortField,
                                      @RequestParam(defaultValue = "asc") String sortDirection,
                                      @RequestParam("itemId") @Min(1) int itemId,
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
    public ModelAndView doPostRemoveItem(@PathVariable(name = "categoryId") @Min(1) Integer categoryId,
                                         @RequestParam(name = "itemId") @Min(1) Integer itemId,
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
        modelMap.addAttribute("tab", NAV_ITEMS_STR);
        return new ModelAndView("redirect:/admin/category/" + categoryId + "/details", modelMap);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> doDelete(@PathVariable(name = "categoryId") @Min(1) Integer categoryId) {
        Optional<Category> optionalCategory = categoryService.readCategoryById(categoryId);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            categoryService.deleteCategory(category);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

//    // TODO use @ControllerAdvice exception handlers for all admin controllers
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException manve) {
//        Map<String, String> errors = new HashMap<>();
//        List<FieldError> fieldErrorList = AdminControllerUtils.getFieldErrorList(manve);
//        for (FieldError fieldError : fieldErrorList) {
//            String fieldName = fieldError.getField();
//            String errorMessage = fieldError.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        }
//        return errors;
//    }

}
