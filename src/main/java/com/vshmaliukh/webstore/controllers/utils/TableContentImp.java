package com.vshmaliukh.webstore.controllers.utils;

import com.vshmaliukh.webstore.model.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.ModelMap;

import java.util.Collection;
import java.util.List;

import static org.springframework.data.domain.Sort.*;


@Slf4j
@Getter
@AllArgsConstructor
public abstract class TableContentImp<T extends AuditModel>
        implements PageableContent, SortableContent, SearchableContent<T> {

    public static final String ASC_DIRECTION_STR = "asc";
    public static final String DESC_DIRECTION_SRT = "desc";

    private int pageSize;
    private int currentPage;
    private String sortField;
    private String sortDirection;
    private String keyword;

    private Direction direction;
    private Order order;
    private Pageable pageable;
    private Page<T> pageWithContent;

    public TableContentImp(String keyword, int currentPage, int pageSize, String sortField, String sortDirection) {
        this.keyword = keyword;
        this.currentPage = currentPage - 1;
        this.pageSize = pageSize;
        this.sortField = sortField;
        this.sortDirection = sortDirection;

        formTableContent();
    }

    public void formTableContent() {
        setUpSortingConfig(sortDirection, sortField);
        setUpPageableConfig(currentPage, pageSize, order);
        pageWithContent = formPageWithContent(keyword, pageable);
    }

    public List<T> readContentList() {
        return pageWithContent.getContent();
    }

    public ModelMap readContentModelMap() {
        ModelMap modelMap = new ModelMap();
        int currentPage = pageWithContent.getNumber() + 1;
        long totalItems = pageWithContent.getTotalElements();
        int totalPages = pageWithContent.getTotalPages();

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
        modelMap.addAttribute("totalItems", totalItems);
        modelMap.addAttribute("totalPages", totalPages);
        modelMap.addAttribute("pageSize", pageSize);
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
