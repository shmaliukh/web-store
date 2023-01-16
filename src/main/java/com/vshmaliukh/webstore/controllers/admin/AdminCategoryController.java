package com.vshmaliukh.webstore.controllers.admin;

import com.vshmaliukh.webstore.model.Category;
import com.vshmaliukh.webstore.model.Image;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.services.CategoryService;
import com.vshmaliukh.webstore.services.ImageService;
import com.vshmaliukh.webstore.services.ItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ModelAndView doGetCatalog(ModelMap modelMap) {
        List<Category> categoryList = categoryService.readAll();

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
                                   @RequestParam(name = "imageFile") MultipartFile imageFile,
                                   ModelMap modelMap) {
        Optional<Image> optionalImage = imageService.buildImageFromFile(imageFile);
        Category category = categoryService.buildBaseCategory(id, name, description, isDeleted, isActivated, optionalImage);
        categoryService.save(category);
        return new ModelAndView("redirect:/admin/category/details/" + category.getId(), modelMap);
    }

    @GetMapping("/details/{categoryId}")
    public ModelAndView doGetDetails(@PathVariable(name = "categoryId") Integer categoryId,
                                     ModelMap modelMap) {
        Optional<Category> optionalCategory = categoryService.readCategoryById(categoryId);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            modelMap.addAttribute("category", category);
            List<Item> itemList = categoryService.readCategoryItemList(category);
            modelMap.addAttribute("itemList", itemList);
            return new ModelAndView("admin/category/details", modelMap);
        }
        return new ModelAndView("admin/category/catalog", modelMap);
    }

}
