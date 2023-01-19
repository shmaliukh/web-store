package com.vshmaliukh.webstore.controllers.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.ModelMap;

public interface PageableContent {

    default Pageable formPageable(int currentPage, int pageSize, Sort.Order order) {
        return PageRequest.of(currentPage, pageSize, Sort.by(order));
    }

    ModelMap formAttributesForPaging(int currentPage,
                                     int pageSize,
                                     long totalItems,
                                     long totalPages);

}
