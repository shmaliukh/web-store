package com.vshmaliukh.webstore.repositories.literature_items_repositories;

import com.vshmaliukh.webstore.model.items.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseItemRepository<T extends Item>  extends JpaRepository<T, Integer> {

    Page<T> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

}
