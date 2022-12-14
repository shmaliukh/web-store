package com.vshmaliukh.webstore;


import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Book;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Comics;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Magazine;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Newspaper;
import com.vshmaliukh.webstore.services.ItemService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class ItemUtil {

    public static final String DATE_FORMAT_STR = "yyyy-MM-dd";

    public static List<String> itemNameList;
    public static List<String> categoryNameList;
    public static Map<String, Class<? extends Item>> itemNameClassTypeMap;

    public static final Class<Book> BOOK_CLASS = Book.class;
    public static final Class<Magazine> MAGAZINE_CLASS = Magazine.class;
    public static final Class<Comics> COMICS_CLASS = Comics.class;
    public static final Class<Newspaper> NEWSPAPER_CLASS = Newspaper.class;

    static {
        generateItemNameClassTypeMap();

        Set<String> keySet = itemNameClassTypeMap.keySet();
        itemNameList = Collections.unmodifiableList(new ArrayList<>(keySet));
    }

    private static void generateItemNameClassTypeMap(){
        Map<String, Class<? extends Item>> nameClassMap = new ConcurrentHashMap<>();
        nameClassMap.put(BOOK_CLASS.getSimpleName(), BOOK_CLASS);
        nameClassMap.put(MAGAZINE_CLASS.getSimpleName(), MAGAZINE_CLASS);
        nameClassMap.put(COMICS_CLASS.getSimpleName(), COMICS_CLASS);
        nameClassMap.put(NEWSPAPER_CLASS.getSimpleName(), NEWSPAPER_CLASS);
        itemNameClassTypeMap = Collections.unmodifiableMap(nameClassMap);
    }

    public static List<Item> readAllItems(ItemService itemService) {
        List<Item> itemAllTypeList = new ArrayList<>();
        for (String itemTypeStr : itemNameList) {
            List<? extends Item> itemListByType = itemService.readAllItemsByTypeName(itemTypeStr);
            if(itemListByType != null){
                itemAllTypeList.addAll(itemListByType);
            }
        }
        return itemAllTypeList;
    }
}
