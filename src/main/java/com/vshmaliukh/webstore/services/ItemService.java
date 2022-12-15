package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.ActionsWithItemRepositoryProvider;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.ActionsWithItem;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ItemService {

    final ActionsWithItemRepositoryProvider actionsWithItemRepositoryProvider;

    public <T extends Item> void saveItem(T item) {
        ActionsWithItem<T> actionsWithItem = actionsWithItemRepositoryProvider.getActionsWithItemRepositoryByItemClassType(item);
        if (actionsWithItem != null) {
            actionsWithItem.save(item);
        } else {
            log.warn("problem to save '{}' item , repository not found", item);
        }
    }

    public boolean isItemSaved(Item item) {
        ActionsWithItem<? extends Item> actionsWithItem = actionsWithItemRepositoryProvider.getActionsWithItemRepositoryByItemClassType(item);
        if (actionsWithItem != null) {
            List<? extends Item> allItemList = actionsWithItem.findAll();
            return allItemList.contains(item);

        } else {
            log.warn("problem to check if the item is saved // item '{}'", item);
        }
        return false;
    }

    public List<? extends Item> readAllItemsByTypeName(String itemTypeName) {
        ActionsWithItem<? extends Item> itemRepositoryByItemTypeName = actionsWithItemRepositoryProvider.getActionsWithItemRepositoryByItemClassName(itemTypeName);
        if (itemRepositoryByItemTypeName != null) {
            return itemRepositoryByItemTypeName.findAll();
        }
        log.warn("problem to read all items by type name // not found repository // itemTypeName: {}", itemTypeName);
        return null;
    }

//    public Integer calcItemsBtCategory(){
//        Map categoryNameQuantityMap = new HashMap<>();
//        Collection<JpaRepository<? extends Item, Integer>> jpaRepositories = itemRepositoryProvider.getItemClassTypeRepositoryMap().values();
//        jpaRepositories.stream()
//                .map(JpaRepository::findAll)
//                .flatMap(Collection::stream)
//                .map(o->categoryNameQuantityMap.put(o.getCategory()))
//                .collect(Collectors.toList());
//
//
//        return null;
//    }


}
