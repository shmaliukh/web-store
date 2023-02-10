package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    boolean existsCategoryByName(String name);

    Page<Category> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

}
