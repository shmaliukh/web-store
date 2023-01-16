package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Category;
import com.vshmaliukh.webstore.model.Image;
import com.vshmaliukh.webstore.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryService {

    final ImageService imageService;
    final CategoryRepository categoryRepository;

    public List<Category> readAll(){
        return categoryRepository.findAll();
    }

    public void save(Category category){
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
                                      boolean isActivated,
                                      Optional<Image> optionalImage) {
        return Category.builder()
                .id(id)
                .name(name)
                .description(description)
                .isDeleted(isDeleted)
                .isActivated(isActivated)
                .image(optionalImage.orElseGet(imageService.getDefaultImage()))
                .build();
    }

}
