package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Category;
import com.vshmaliukh.webstore.model.Image;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Setter
@Service
@NoArgsConstructor
@AllArgsConstructor
public class CategoryService {

    protected ImageService imageService;

    @Getter
    protected CategoryRepository categoryRepository;

    public List<Category> readAll() {
        return categoryRepository.findAll();
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public List<String> readCategoryNameList() {
        return Collections.unmodifiableList(
                readAll().stream()
                        .filter(Objects::nonNull)
                        .map(Category::getName)
                        .collect(Collectors.toList())
        );
    }

    public Category getUpdatedOrCreateBaseCategory(Integer id,
                                                   String name, String description,
                                                   boolean isDeleted, boolean isActivated) {
        Optional<Category> optionalCategory = readCategoryById(id);
        if (optionalCategory.isPresent()) {
            Optional<Category> updatedCategory
                    = updateCategory(name, description, isDeleted, isActivated, optionalCategory.get());
            if (updatedCategory.isPresent()) {
                return updatedCategory.get();
            }
        }
        return new Category(name, description);
    }

    public Optional<Category> updateCategory(String name, String description,
                                             Boolean isDeleted, Boolean isActivated,
                                             Category category) {
        if (StringUtils.isNotBlank(name)
                && StringUtils.isNotBlank(description)
                && isDeleted != null
                && isActivated != null
                && category != null) {
            category.setName(name);
            category.setDescription(description);
            category.setActivated(isActivated);
            category.setDeleted(isDeleted);
            return Optional.of(category);
        }
        log.warn("problem to update category"
                + (StringUtils.isNotBlank(name) ? " // name is blank" : "")
                + (StringUtils.isNotBlank(description) ? " // description is blank" : "")
                + (isDeleted == null ? " // isDeleted is NULL" : "")
                + (isActivated == null ? " // isActivated is NULL" : "")
                + (category == null ? " // category is NULL" : ""));
        return Optional.empty();
    }

    public Optional<Category> readCategoryById(Integer categoryId) {
        if (categoryId != null && categoryId > 0) {
            return categoryRepository.findById(categoryId);
        }
        log.warn("problem to read category by id "
                + (categoryId == null ? " // categoryId is NULL" : " // categoryId must be greater than 0"));
        return Optional.empty();
    }

    public void addImageToCategory(Long imageId, MultipartFile imageFile, Category category) {
        Optional<Image> optionalImage = imageService.buildImageFromFile(imageFile);
        if (optionalImage.isPresent()) {
            Image image = optionalImage.get();
            image.setId(imageId);
            category.setImage(image);
            save(category);
        } else {
            log.warn("problem to add image to '{}' category", category);
        }
    }

    public ResponseEntity<Void> deleteImageByCategoryId(Integer categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setImage(null);
            categoryRepository.save(category);
            log.info("deleted '{}' category image", category);
            return ResponseEntity.ok().build();
        }
        log.warn("problem to delete category (with '{}' id) image", categoryId);
        return ResponseEntity.badRequest().build();
    }

    public void addItemToCategory(Item item, Category category) {
        Set<Item> categoryItemSet = category.getItemSet();
        categoryItemSet.add(item);
        category.setItemSet(categoryItemSet);
        categoryRepository.save(category);
        log.info("added '{}' item to '{}' category", item, category);
    }

    public void removeItemFromCategory(Item item, Category category) {
        Set<Item> categoryItemSet = category.getItemSet();
        categoryItemSet.remove(item);
        category.setItemSet(categoryItemSet);
        categoryRepository.save(category);
        log.info("removed '{}' item from '{}' category", item, category);
    }

    public void deleteCategory(Category category) {
        // TODO is it normal to set up category instance as deleted one
        category.setDeleted(true);
        save(category);
        log.info("deleted category: '{}'", category);
    }

}
