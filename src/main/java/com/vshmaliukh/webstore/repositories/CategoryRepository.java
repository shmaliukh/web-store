package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
