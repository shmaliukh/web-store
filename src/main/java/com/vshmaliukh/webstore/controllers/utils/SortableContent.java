package com.vshmaliukh.webstore.controllers.utils;

import org.springframework.ui.ModelMap;

public interface SortableContent {

    void setUpSortingConfig(String sortDirection, String sortField);

    ModelMap formAttributesForSorting(String sortField, String sortDirection);

}
