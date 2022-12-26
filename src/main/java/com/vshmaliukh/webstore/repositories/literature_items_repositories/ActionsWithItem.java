package com.vshmaliukh.webstore.repositories.literature_items_repositories;

import com.vshmaliukh.webstore.ConstantsForEntities;
import com.vshmaliukh.webstore.model.items.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ActionsWithItem<T extends Item> {

    void deleteById(@Param(ConstantsForEntities.ITEM_ID_COLUMN) Integer itemId);

    T save(T itemEntity);

    List<T> findAll();

    Page<T> findAll(Pageable pageable);

    Page<T> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

}
