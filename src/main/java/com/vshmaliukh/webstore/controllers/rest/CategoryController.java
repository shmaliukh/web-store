package com.vshmaliukh.webstore.controllers.rest;

import com.vshmaliukh.webstore.services.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController("/category")
@AllArgsConstructor
public class CategoryController {

    final CategoryService categoryService;

    @GetMapping("/names")
    public ResponseEntity<List<String>> readCatalogNameList(){
        List<String> categoryNameList = categoryService.readCategoryNameList();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(categoryNameList);
    }

}
