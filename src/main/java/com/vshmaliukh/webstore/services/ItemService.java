package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.ItemRepositoryProvider;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ItemService {

    final ItemRepositoryProvider itemRepositoryProvider;

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
