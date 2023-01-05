package com.vshmaliukh.webstore.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryService {

    // TODO implement 'category' entity
    public List<String> readCategoryNameList() {
        return Arrays.asList(
                "Fiction",
                "Mystery",
                "Thriller",
                "Horror",
                "Historical",
                "Romance",
                "Western",
                "Bildungsroman",
                "Speculative Fiction",
                "Science Fiction",
                "Fantasy",
                "Dystopian",
                "Magical Realism",
                "Realist Literature"
        );
    }

}
