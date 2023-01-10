package com.vshmaliukh.webstore;

import com.vshmaliukh.webstore.model.items.Item;

import java.util.List;

public final class CustomUtils {

    private CustomUtils(){}

    public static int calcItemListTotalPrice(List<Item> itemList) {
        return itemList.stream().mapToInt(Item::getSalePrice).sum();
    }

}
