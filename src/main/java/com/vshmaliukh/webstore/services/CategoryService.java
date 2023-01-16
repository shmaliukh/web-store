package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Category;
import com.vshmaliukh.webstore.model.Image;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryService {

    final ImageService imageService;
    final CategoryRepository categoryRepository;

    public List<Category> readAll() {
        return categoryRepository.findAll();
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    // TODO implement 'category' entity
    public List<String> readCategoryNameList() {
        return readAll().stream()
                .map(Category::getName)
                .collect(Collectors.toList());
//        return Arrays.asList(
//                "Fiction",
//                "Mystery",
//                "Thriller",
//                "Horror",
//                "Historical",
//                "Romance",
//                "Western",
//                "Bildungsroman",
//                "Speculative Fiction",
//                "Science Fiction",
//                "Fantasy",
//                "Dystopian",
//                "Magical Realism",
//                "Realist Literature"
//        );
    }

    public Category buildBaseCategory(Integer id,
                                      String name,
                                      String description,
                                      boolean isDeleted,
                                      boolean isActivated) {
        return Category.builder()
                .id(id)
                .name(name)
                .description(description)
                .isDeleted(isDeleted)
                .isActivated(isActivated)
                .build();
    }

    public Optional<Category> readCategoryById(Integer categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public List<Item> readCategoryItemList(Category category) {
        // FIXME read item list via repository
        return Collections.EMPTY_LIST;
    }

    public void addImageToCategory(MultipartFile imageFile, Category category) {
        Optional<Image> optionalImage = imageService.buildImageFromFile(imageFile);
        if (optionalImage.isPresent()) {
            Image image = optionalImage.get();
            category.setImage(image);
            save(category);
        } else{
            log.warn("problem to add image to '{}' category", category);
        }
    }
}
