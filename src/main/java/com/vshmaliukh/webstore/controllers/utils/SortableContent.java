package com.vshmaliukh.webstore.controllers.utils;

import org.springframework.ui.ModelMap;

public interface SortableContent {

    void addAttributesForSorting(String sortField,
                                 String sortDirection,
                                 ModelMap modelMap);

}
