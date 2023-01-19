package com.vshmaliukh.webstore.controllers.utils;

import com.vshmaliukh.webstore.model.AuditModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.ModelMap;

public interface SearchableContent<T extends AuditModel> {

    Page<T> formPageIfKeywordIsBlank(Pageable pageable);

    Page<T> formPageWithContentByKeyword(String keyword, Pageable pageable);

    ModelMap formAttributesForSearching(String keyword);

}
