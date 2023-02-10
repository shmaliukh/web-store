package com.vshmaliukh.webstore.controllers.rest;

import com.vshmaliukh.webstore.dto.CategoryDto;
import com.vshmaliukh.webstore.model.Category;
import com.vshmaliukh.webstore.services.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
public class CategoryController {

    final CategoryService categoryService;

    @GetMapping("/category/names")
    public ResponseEntity<List<String>> readCatalogNameList() {
        List<String> categoryNameList = categoryService.readCategoryNameList();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(categoryNameList);
    }

    @GetMapping("/category/find-by-name")
    public ResponseEntity<CategoryDto> readCategoryByName(@RequestBody String name) {
        Optional<Category> optionalCategory = categoryService.readCategoryByName(name);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new CategoryDto(category));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/category/find-by-id")
    public ResponseEntity<CategoryDto> readCategoryById(@RequestBody Integer id) {
        Optional<Category> optionalCategory = categoryService.readCategoryById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new CategoryDto(category));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/category/all")
    public ResponseEntity<List<CategoryDto>> readAllCategories() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(categoryService.readAllAsDto());
    }

}
