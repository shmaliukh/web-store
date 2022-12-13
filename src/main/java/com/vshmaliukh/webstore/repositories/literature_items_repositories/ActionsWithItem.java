package com.vshmaliukh.webstore.repositories.literature_items_repositories;

import com.vshmaliukh.webstore.ConstantsForEntities;
import com.vshmaliukh.webstore.model.items.Item;
import org.springframework.data.repository.query.Param;


public interface ActionsWithItem<T extends Item> {

    void deleteById(@Param(ConstantsForEntities.ITEM_ID_COLUMN) Integer itemId);

    T save(T itemEntity);

}
