package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Category;
import com.vshmaliukh.webstore.model.Image;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.CategoryRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryService implements EntityValidator<Category> {

    @Getter
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> readAll() {
        return Collections.unmodifiableList(categoryRepository.findAll());
    }

    public void save(Category category) {
        if (isValidEntity(category)) {
            categoryRepository.save(category);
            log.info("saved category: {}", category);
        } else {
            log.error("problem to save category // invalid category");
        }
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
        log.error("problem to read category by id "
                + (categoryId == null ? " // categoryId is NULL" : " // categoryId must be greater than 0"));
        return Optional.empty();
    }

    public void addImageToCategory(Long imageId, Optional<Image> optionalImage, Category category) {
        if (optionalImage.isPresent() && isValidEntity(category)) {
            Image image = optionalImage.get();
            image.setId(imageId);
            category.setImage(image);
            save(category);
        } else {
            log.error("problem to add image to '{}' category"
                    + (!optionalImage.isPresent() ? " // image is not present" : "")
                    + (!isValidEntity(category) ? " // invalid category" : ""), category);
        }
    }

    public ResponseEntity<Void> deleteImageByCategory(Optional<Category> optionalCategory) {
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setImage(null);
            categoryRepository.save(category);
            log.info("deleted '{}' category image", category);
            return ResponseEntity.ok().build();
        }
        log.error("problem to delete category image // category not found");
        return ResponseEntity.badRequest().build();
    }

    public void addItemToCategory(Item item, Category category) {
        if (item != null && category != null) {
            Set<Item> categoryItemSet = category.getItemSet();
            HashSet<Item> buffItemHashSet = new HashSet<>(categoryItemSet);
            buffItemHashSet.add(item);
            category.setItemSet(buffItemHashSet);
            categoryRepository.save(category);
            log.info("item '{}' added to category '{}'", item, category);
        } else {
            log.error("problem to add item to category"
                    + (item == null ? " // item is NULL" : " // category is NULL"));
        }
    }

    public void removeItemFromCategory(Item item, Category category) {
        if (item != null && category != null) {
            Set<Item> categoryItemSet = category.getItemSet();
            if (categoryItemSet.contains(item)) {
                HashSet<Item> buffItemHashSet = new HashSet<>(categoryItemSet);
                buffItemHashSet.remove(item);
                category.setItemSet(buffItemHashSet);
                categoryRepository.save(category);
                log.info(" item '{}' removed from category '{}'", item, category);
            } else {
                log.warn("problem to remove item from category"
                        + " // '{}' category does not contain '{}' item", category, item);
            }
        } else {
            log.error("problem to remove item from category"
                    + (item == null ? " // item is NULL" : " // category is NULL"));
        }
    }

    public void deleteCategory(Category category) {
        // TODO is it normal to set up category instance state as deleted one
        if (category != null && !category.isDeleted()) {
            category.setDeleted(true);
            save(category);
            log.info("deleted category: '{}'", category);
        } else {
            log.error("problem to delete category"
                    + (category == null ? " // category is NULL" : "category already deleted"));
        }
    }

}
