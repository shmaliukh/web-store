package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.ItemRepositoryProvider;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.BaseItemRepository;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.ItemRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class ItemService {

    final ItemRepositoryProvider itemRepositoryProvider;

    public Optional<Item> readItemById(Integer itemId){
        ItemRepository allItemRepository = itemRepositoryProvider.getAllItemRepository();
        return allItemRepository.findById(itemId);
    }

    // TODO implement read items via repository
    public List<Item> readItemsAvailableToBuy(){
        ItemRepository allItemRepository = itemRepositoryProvider.getAllItemRepository();
//        return allItemRepository.findAllByQuantityGreaterThanEqualAndAvailableInStoreEquals(1, true);
        return allItemRepository.findAll().stream()
                .filter(Item::isAvailableInStore)
                .filter(item -> item.getQuantity() > 0)
                .collect(Collectors.toList());
    }

    public <T extends Item> void saveItem(T item) {
        BaseItemRepository<T> baseItemRepository = itemRepositoryProvider.getItemRepositoryByItemClassType(item);
        if (baseItemRepository != null) {
            if(item.getQuantity() < 1){
                item.setAvailableInStore(false);
            }
            baseItemRepository.save(item);
        } else {
            log.warn("problem to save '{}' item , repository not found", item);
        }
    }

    public boolean isItemSaved(Item item) {
        BaseItemRepository<? extends Item> baseItemRepository = itemRepositoryProvider.getItemRepositoryByItemClassType(item);
        if (baseItemRepository != null) {
            List<? extends Item> allItemList = baseItemRepository.findAll();
            return allItemList.contains(item);
        } else {
            log.warn("problem to check if the item is saved // item '{}'", item);
        }
        return false;
    }

    public List<? extends Item> readAllItemsByTypeName(String itemTypeName) {
        BaseItemRepository<? extends Item> itemRepositoryByItemTypeNameByType = itemRepositoryProvider.getItemRepositoryByItemClassName(itemTypeName);
        if (itemRepositoryByItemTypeNameByType != null) {
            return itemRepositoryByItemTypeNameByType.findAll();
        }
        log.warn("problem to read all items by type name // not found repository // itemTypeName: {}", itemTypeName);
        return null;
    }

    public <T extends Item> void deleteItem(T item) {
        BaseItemRepository<T> baseItemRepository = itemRepositoryProvider.getItemRepositoryByItemClassType(item);
        if (baseItemRepository != null) {
            Integer itemId = item.getId();
            if (itemId != null) {
                baseItemRepository.deleteById(itemId);
            } else{
                log.warn("problem to save '{}' item , item id is NULL", item);
            }
        } else {
            log.warn("problem to save '{}' item , repository not found", item);
        }
    }

}
