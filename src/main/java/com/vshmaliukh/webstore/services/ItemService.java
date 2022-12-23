package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.ItemRepositoryProvider;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.BaseItemRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class ItemService {

    final ItemRepositoryProvider itemRepositoryProvider;

    public Item readItemByIdAndType(Integer id, String type){
        BaseItemRepository<? extends Item> repositoryByType = itemRepositoryProvider.getItemRepositoryByItemClassName(type);
        if(repositoryByType != null){
            Optional<? extends Item> optionalItem = repositoryByType.findById(id);
            if(optionalItem.isPresent()){
                return optionalItem.get();
            } else {
                log.warn("problem to find '{}' type item by '{}' id", type, id);
            }
        } else {
            log.warn("problem to find repository by '{}' type", type);
        }
        return null;
    }

    public <T extends Item> void saveItem(T item) {
        BaseItemRepository<T> baseItemRepository = itemRepositoryProvider.getItemRepositoryByItemClassType(item);
        if (baseItemRepository != null) {
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
