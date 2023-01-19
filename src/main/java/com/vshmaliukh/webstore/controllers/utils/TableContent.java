package com.vshmaliukh.webstore.controllers.utils;

import com.vshmaliukh.webstore.model.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.ModelMap;

import java.util.List;

import static org.springframework.data.domain.Sort.*;


@Slf4j
@Getter
@AllArgsConstructor
public abstract class TableContent<T extends AuditModel>
        implements PageableContent, SortableContent, SearchableContent<T> {

    public static final String ASC_DIRECTION_STR = "asc";
    public static final String DESC_DIRECTION_SRT = "desc";

    private int currentPage;
    private int pageSize;
    private long totalItems;
    private long totalPages;
    private String sortField;
    private String sortDirection;
    private String keyword;

    private Direction direction;
    private Order order;
    private Pageable pageable;
    private Page<T> pageWithContent;

    public TableContent(int currentPage, int pageSize,
                        long totalItems, long totalPages,
                        String sortField, String sortDirection, String keyword) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.sortField = sortField;
        this.sortDirection = sortDirection;
        this.keyword = keyword;

        formTableContent();
    }

    public void formTableContent() {
        setUpSortingConfig(sortDirection, sortField);
        setUpPageableConfig(currentPage, pageSize, order);
        pageWithContent = formPageWithContent(keyword, pageable);
    }

    public List<T> readContent() {
        return pageWithContent.getContent();
    }

    public ModelMap readModelMap() {
        ModelMap modelMap = new ModelMap();
        modelMap.addAllAttributes(formAttributesForPaging(currentPage, pageSize, totalItems, totalPages));
        modelMap.addAllAttributes(formAttributesForSorting(sortField, sortDirection));
        modelMap.addAllAttributes(formAttributesForSearching(keyword));
        return modelMap;
    }

    public Page<T> formPageWithContent(String keyword, Pageable pageable) {
        return StringUtils.isBlank(keyword)
                ? formPageIfKeywordIsBlank(pageable)
                : formPageWithContentByKeyword(keyword, pageable);
    }

    @Override
    public void setUpSortingConfig(String sortDirection, String sortField) {
        direction = sortDirection.equalsIgnoreCase(DESC_DIRECTION_SRT) ? Direction.DESC : Direction.ASC;
        order = new Order(direction, sortField);
    }

    @Override
    public void setUpPageableConfig(int currentPage, int pageSize, Order order) {
        pageable = formPageable(currentPage, pageSize, order);
    }

    @Override
    public ModelMap formAttributesForPaging(int currentPage, int pageSize, long totalItems, long totalPages) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("currentPage", currentPage);
        modelMap.addAttribute("pageSize", pageSize);
        modelMap.addAttribute("totalItems", totalItems);
        modelMap.addAttribute("totalPages", totalPages);
        return modelMap;
    }

    @Override
    public ModelMap formAttributesForSorting(String sortField, String sortDirection) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("sortField", sortField);
        modelMap.addAttribute("sortDirection", sortDirection);
        modelMap.addAttribute("reverseSortDirection",
                sortDirection.equalsIgnoreCase(ASC_DIRECTION_STR) ? DESC_DIRECTION_SRT : ASC_DIRECTION_STR);
        return modelMap;
    }

    @Override
    public ModelMap formAttributesForSearching(String keyword) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("keyword", keyword);
        return modelMap;
    }

}
