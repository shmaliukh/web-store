package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.ActionsWithItemRepositoryProvider;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.ActionsWithItem;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class ItemService {

    final ActionsWithItemRepositoryProvider actionsWithItemRepositoryProvider;

    public Item readItemByIdAndType(Integer id, String type){
        ActionsWithItem<? extends Item> repositoryByType = actionsWithItemRepositoryProvider.getActionsWithItemRepositoryByItemClassName(type);
        if(repositoryByType != null){
            Optional<? extends Item> optionalItem = repositoryByType.findById(id);
            if(optionalItem.isPresent()){
                return optionalItem.get();
            } else {
                log.warn("problem to find '{}' item by '{}' id", id, type);
            }
        } else {
            log.warn("problem to find repository by '{}' type", type);
        }
        return null;
    }

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

    public <T extends Item> void deleteItem(T item) {
        ActionsWithItem<T> actionsWithItem = actionsWithItemRepositoryProvider.getActionsWithItemRepositoryByItemClassType(item);
        if (actionsWithItem != null) {
            Integer itemId = item.getId();
            if (itemId != null) {
                actionsWithItem.deleteById(itemId);
            } else{
                log.warn("problem to save '{}' item , item id is NULL", item);
            }
        } else {
            log.warn("problem to save '{}' item , repository not found", item);
        }
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
