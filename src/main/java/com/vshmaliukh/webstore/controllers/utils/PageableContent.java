package com.vshmaliukh.webstore.controllers.utils;

import org.springframework.ui.ModelMap;

public interface PageableContent {

    void addAttributesForPaging(int currentPage,
                                int pageSize,
                                long totalItems,
                                long totalPages,
                                ModelMap modelMap);

}
