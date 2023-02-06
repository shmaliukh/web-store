package com.vshmaliukh.webstore.repositories.literature_items_repositories;

import com.vshmaliukh.webstore.model.Category;
import com.vshmaliukh.webstore.model.items.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends BaseItemRepository<Item> {

    Page<Item> readAllByCategorySetContaining(Category category, Pageable pageable);

    Page<Item> readAllByCategorySetContainingAndNameContainingIgnoreCase(Category category, String keyword, Pageable pageable);

}
