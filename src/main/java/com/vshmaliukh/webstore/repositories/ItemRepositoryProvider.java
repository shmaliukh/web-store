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
import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
public final class ItemRepositoryProvider {

    final BookRepository bookRepository;
    final ComicsRepository comicsRepository;
    final MagazineRepository magazineRepository;
    final NewspaperRepository newspaperRepository;

    public Map<Class<? extends Item>, JpaRepository<? extends Item, Integer>> itemClassTypeRepositoryMap;
    List<JpaRepository<? extends Item, Integer>> itemRepositoryList;

    public ItemRepositoryProvider(BookRepository bookRepository,
                                  ComicsRepository comicsRepository,
                                  MagazineRepository magazineRepository,
                                  NewspaperRepository newspaperRepository) {
        this.bookRepository = bookRepository;
        this.comicsRepository = comicsRepository;
        this.magazineRepository = magazineRepository;
        this.newspaperRepository = newspaperRepository;
    }

    @PostConstruct
    private void postConstruct() {
        generateItemClassTypeRepositoryMap();
        generateItemRepositoryList();
    }

    private void generateItemRepositoryList() {
        itemRepositoryList = Collections.unmodifiableList(new ArrayList<>(itemClassTypeRepositoryMap.values()));
    }

    private void generateItemClassTypeRepositoryMap() {
        Map<Class<? extends Item>, JpaRepository<? extends Item, Integer>> classTypeRepositoryMap = new ConcurrentHashMap<>();
        classTypeRepositoryMap.put(Book.class, bookRepository);
        classTypeRepositoryMap.put(Magazine.class, magazineRepository);
        classTypeRepositoryMap.put(Comics.class, comicsRepository);
        classTypeRepositoryMap.put(Newspaper.class, newspaperRepository);
        itemClassTypeRepositoryMap = Collections.unmodifiableMap(classTypeRepositoryMap);
    }

}
