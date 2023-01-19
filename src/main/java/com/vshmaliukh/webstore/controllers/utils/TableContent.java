package com.vshmaliukh.webstore.controllers.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@Slf4j
@Service
@AllArgsConstructor
public class TableContent implements PageableContent, SortableContent {

    public static final String ASC_DIRECTION = "asc";
    public static final String DESC_DIRECTION = "desc";

    @Override
    public void addAttributesForPaging(int currentPage, int pageSize, long totalItems, long totalPages, ModelMap modelMap) {
        modelMap.addAttribute("currentPage", currentPage);
        modelMap.addAttribute("pageSize", pageSize);
        modelMap.addAttribute("totalItems", totalItems);
        modelMap.addAttribute("totalPages", totalPages);
    }

    @Override
    public void addAttributesForSorting(String sortField, String sortDirection, ModelMap modelMap) {
        modelMap.addAttribute("sortField", sortField);
        modelMap.addAttribute("sortDirection", sortDirection);
        modelMap.addAttribute("reverseSortDirection", sortDirection.equalsIgnoreCase(ASC_DIRECTION) ? DESC_DIRECTION : ASC_DIRECTION);
    }

}
