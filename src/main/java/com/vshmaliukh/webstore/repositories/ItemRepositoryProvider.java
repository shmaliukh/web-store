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
public final class ItemRepositoryProvider {

    // TODO refactor

    final ItemRepository<Book> bookRepository;
    final ItemRepository<Comics> comicsRepository;
    final ItemRepository<Magazine> magazineRepository;
    final ItemRepository<Newspaper> newspaperRepository;

    public List<ItemRepository<? extends Item>> itemRepositoryList;
    public Map<Class<? extends Item>, ItemRepository<? extends Item>> itemClassTypeRepositoryMap;
    public Map<String, ItemRepository<? extends Item>> itemClassNameRepositoryMap;

    public ItemRepositoryProvider(ItemRepository<Book> bookRepository,
                                  ItemRepository<Comics> comicsRepository,
                                  ItemRepository<Magazine> magazineRepository,
                                  ItemRepository<Newspaper> newspaperRepository) {
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
        itemRepositoryList = Collections.unmodifiableList(new ArrayList<>(itemClassTypeRepositoryMap.values()));
    }

    private void generateItemClassTypeRepositoryMap() {
        Map<Class<? extends Item>, ItemRepository<? extends Item>> classTypeRepositoryMap = new ConcurrentHashMap<>();
        classTypeRepositoryMap.put(Book.class, bookRepository);
        classTypeRepositoryMap.put(Magazine.class, magazineRepository);
        classTypeRepositoryMap.put(Comics.class, comicsRepository);
        classTypeRepositoryMap.put(Newspaper.class, newspaperRepository);
        itemClassTypeRepositoryMap = Collections.unmodifiableMap(classTypeRepositoryMap);
    }

    private void generateItemClassNameRepositoryMap() {
        Map<String, ItemRepository<? extends Item>> classNameRepositoryMap = new ConcurrentHashMap<>();
        classNameRepositoryMap.put(Book.class.getSimpleName().toLowerCase(), bookRepository);
        classNameRepositoryMap.put(Magazine.class.getSimpleName().toLowerCase(), magazineRepository);
        classNameRepositoryMap.put(Comics.class.getSimpleName().toLowerCase(), comicsRepository);
        classNameRepositoryMap.put(Newspaper.class.getSimpleName().toLowerCase(), newspaperRepository);
        itemClassNameRepositoryMap = Collections.unmodifiableMap(classNameRepositoryMap);
    }

    public <T extends Item> ItemRepository<T> getItemRepositoryByItemClassType(T item) {
        return (ItemRepository<T>) itemClassTypeRepositoryMap.getOrDefault(item.getClass(), null);

    }

    public ItemRepository<? extends Item> getItemRepositoryByItemClassName(String itemClassName) {
        return itemClassNameRepositoryMap.getOrDefault(itemClassName.toLowerCase(), null);
    }

}
