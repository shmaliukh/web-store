package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Book;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Comics;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Magazine;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Newspaper;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.BookRepository;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.ComicsRepository;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.MagazineRepository;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.NewspaperRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@AllArgsConstructor
public final class ItemRepositoryProvider {

//    final BookRepository bookRepository;
//    final ComicsRepository comicsRepository;
//    final MagazineRepository magazineRepository;
//    final NewspaperRepository newspaperRepository;
//
//    public final Map<Class<? extends Item>, JpaRepository<? extends Item, Integer>> itemClassTypeRepositoryMap = Collections.unmodifiableMap(generateItemClassTypeRepositoryMap());
//
//    private Map<Class<? extends Item>, JpaRepository<? extends Item, Integer>> generateItemClassTypeRepositoryMap(){
//        Map<Class<? extends Item>, JpaRepository<? extends Item, Integer>> classTypeRepositoryMap = new ConcurrentHashMap<>();
//        classTypeRepositoryMap.put(Book.class, bookRepository);
//        classTypeRepositoryMap.put(Magazine.class, magazineRepository);
//        classTypeRepositoryMap.put(Comics.class, comicsRepository);
//        classTypeRepositoryMap.put(Newspaper.class, newspaperRepository);
//        return classTypeRepositoryMap;
//    }

}
