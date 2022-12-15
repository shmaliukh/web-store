package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Book;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Comics;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Magazine;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Newspaper;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.*;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
public final class ActionsWithItemRepositoryProvider {

    // TODO refactor

    final ActionsWithItem<Book> bookRepository;
    final ActionsWithItem<Comics> comicsRepository;
    final ActionsWithItem<Magazine> magazineRepository;
    final ActionsWithItem<Newspaper> newspaperRepository;

    public List<ActionsWithItem<? extends Item>> itemActionsWithRepositoryList;
    public Map<Class<? extends Item>, ActionsWithItem<? extends Item>> itemClassTypeActionsWithRepositoryMap;
    public Map<String, ActionsWithItem<? extends Item>> itemClassNameActionsWithRepositoryMap;

    public ActionsWithItemRepositoryProvider(ActionsWithItem<Book> bookRepository,
                                             ActionsWithItem<Comics> comicsRepository,
                                             ActionsWithItem<Magazine> magazineRepository,
                                             ActionsWithItem<Newspaper> newspaperRepository) {
        this.bookRepository = bookRepository;
        this.comicsRepository = comicsRepository;
        this.magazineRepository = magazineRepository;
        this.newspaperRepository = newspaperRepository;
    }


    @PostConstruct
    private void postConstruct() {
        generateItemClassTypeRepositoryMap();
        generateItemClassNameRepositoryMap();
        generateItemRepositoryList();
    }

    private void generateItemRepositoryList() {
        itemActionsWithRepositoryList = Collections.unmodifiableList(new ArrayList<>(itemClassTypeActionsWithRepositoryMap.values()));
    }

    private void generateItemClassTypeRepositoryMap() {
        Map<Class<? extends Item>, ActionsWithItem<? extends Item>> classTypeRepositoryMap = new ConcurrentHashMap<>();
        classTypeRepositoryMap.put(Book.class, bookRepository);
        classTypeRepositoryMap.put(Magazine.class, magazineRepository);
        classTypeRepositoryMap.put(Comics.class, comicsRepository);
        classTypeRepositoryMap.put(Newspaper.class, newspaperRepository);
        itemClassTypeActionsWithRepositoryMap = Collections.unmodifiableMap(classTypeRepositoryMap);
    }

    private void generateItemClassNameRepositoryMap() {
        Map<String, ActionsWithItem<? extends Item>> classNameRepositoryMap = new ConcurrentHashMap<>();
        classNameRepositoryMap.put(Book.class.getSimpleName().toLowerCase(), bookRepository);
        classNameRepositoryMap.put(Magazine.class.getSimpleName().toLowerCase(), magazineRepository);
        classNameRepositoryMap.put(Comics.class.getSimpleName().toLowerCase(), comicsRepository);
        classNameRepositoryMap.put(Newspaper.class.getSimpleName().toLowerCase(), newspaperRepository);
        itemClassNameActionsWithRepositoryMap = Collections.unmodifiableMap(classNameRepositoryMap);
    }

    public <T extends Item> ActionsWithItem<T> getActionsWithItemRepositoryByItemClassType(T item) {
        return (ActionsWithItem<T>) itemClassTypeActionsWithRepositoryMap.getOrDefault(item.getClass(), null);

    }

    public ActionsWithItem<? extends Item> getActionsWithItemRepositoryByItemClassName(String itemClassName) {
        return itemClassNameActionsWithRepositoryMap.getOrDefault(itemClassName.toLowerCase(), null);
    }

}
