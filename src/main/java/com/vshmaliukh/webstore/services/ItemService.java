package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.ActionsWithItemRepositoryProvider;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.ActionsWithItem;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class ItemService {

    final ActionsWithItemRepositoryProvider actionsWithItemRepositoryProvider;

    public <T extends Item> void saveItem(T item){
        ActionsWithItem<T> actionsWithItem = actionsWithItemRepositoryProvider.getActionsWithItemRepositoryByItemClassType(item);
        if(actionsWithItem != null){
            actionsWithItem.save(item);
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
