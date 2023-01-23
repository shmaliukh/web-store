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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Setter
@Service
@NoArgsConstructor
@AllArgsConstructor
public class CategoryService {

    ImageService imageService;

    @Getter
    CategoryRepository categoryRepository;

    public List<Category> readAll() {
        return categoryRepository.findAll();
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public List<String> readCategoryNameList() {
        return readAll().stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }

    public Category buildBaseCategory(String name,
                                      String description) {
        return new Category(name, description);
    }

    public Category getUpdatedOrCreateBaseCategory(Integer id,
                                                   String name,
                                                   String description,
                                                   boolean isDeleted,
                                                   boolean isActivated) {
        if (id != null) {
            Optional<Category> optionalCategory = readCategoryById(id);
            if (optionalCategory.isPresent()) {
                return getUpdatedCategory(name, description, isDeleted, isActivated, optionalCategory.get());
            }
        }
        return buildBaseCategory(name, description);
    }

    public Category getUpdatedCategory(String name,
                                       String description,
                                       boolean isDeleted,
                                       boolean isActivated,
                                       Category category) {
        category.setName(name);
        category.setDescription(description);
        category.setActivated(isActivated);
        category.setDeleted(isDeleted);
        return category;
    }

    public Optional<Category> readCategoryById(Integer categoryId) {
        return categoryRepository.findById(categoryId);
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
