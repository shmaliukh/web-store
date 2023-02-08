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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
public class ItemRepositoryProvider {

    // TODO refactor

    final ItemRepository allItemRepository;

    final BaseItemRepository<Book> bookRepository;
    final BaseItemRepository<Comics> comicsRepository;
    final BaseItemRepository<Magazine> magazineRepository;
    final BaseItemRepository<Newspaper> newspaperRepository;

    public List<BaseItemRepository<? extends Item>> baseItemRepositoryList;
    public Map<Class<? extends Item>, BaseItemRepository<? extends Item>> itemClassTypeRepositoryMap;
    public Map<String, BaseItemRepository<? extends Item>> itemClassNameRepositoryMap;

    public ItemRepositoryProvider(ItemRepository allItemRepository,
                                  BaseItemRepository<Book> bookRepository,
                                  BaseItemRepository<Comics> comicsRepository,
                                  BaseItemRepository<Magazine> magazineRepository,
                                  BaseItemRepository<Newspaper> newspaperRepository) {
        this.allItemRepository = allItemRepository;
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

    public Set<String> readTypeNameSet() {
        return Collections.unmodifiableSet(itemClassNameRepositoryMap.keySet());
    }

    private void generateItemRepositoryList() {
        baseItemRepositoryList = Collections.unmodifiableList(new ArrayList<>(itemClassTypeRepositoryMap.values()));
    }

    private void generateItemClassTypeRepositoryMap() {
        Map<Class<? extends Item>, BaseItemRepository<? extends Item>> classTypeRepositoryMap = new ConcurrentHashMap<>();
        classTypeRepositoryMap.put(Book.class, bookRepository);
        classTypeRepositoryMap.put(Magazine.class, magazineRepository);
        classTypeRepositoryMap.put(Comics.class, comicsRepository);
        classTypeRepositoryMap.put(Newspaper.class, newspaperRepository);
        itemClassTypeRepositoryMap = Collections.unmodifiableMap(classTypeRepositoryMap);
    }

    private void generateItemClassNameRepositoryMap() {
        Map<String, BaseItemRepository<? extends Item>> classNameRepositoryMap = new ConcurrentHashMap<>();
        classNameRepositoryMap.put(Book.class.getSimpleName().toLowerCase(), bookRepository);
        classNameRepositoryMap.put(Magazine.class.getSimpleName().toLowerCase(), magazineRepository);
        classNameRepositoryMap.put(Comics.class.getSimpleName().toLowerCase(), comicsRepository);
        classNameRepositoryMap.put(Newspaper.class.getSimpleName().toLowerCase(), newspaperRepository);
        itemClassNameRepositoryMap = Collections.unmodifiableMap(classNameRepositoryMap);
    }

    public <T extends Item> BaseItemRepository<T> getItemRepositoryByItemClassType(T item) {
        return (BaseItemRepository<T>) itemClassTypeRepositoryMap.getOrDefault(item.getClass(), null);
    }

    public BaseItemRepository<? extends Item> getItemRepositoryByItemClassName(String itemClassName) {
        return itemClassNameRepositoryMap.getOrDefault(itemClassName.toLowerCase(), allItemRepository);
    }

}
