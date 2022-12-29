package com.vshmaliukh.webstore.repositories.literature_items_repositories;

import com.vshmaliukh.webstore.model.items.Item;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends BaseItemRepository<Item> {

//    List<Item> findAllByQuantityGreaterThanEqualAndAvailableInStoreEquals(Integer minQuantity, Boolean available);

}
