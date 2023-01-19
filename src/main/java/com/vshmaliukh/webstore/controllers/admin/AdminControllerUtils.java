package com.vshmaliukh.webstore.controllers.admin;

import com.vshmaliukh.webstore.controllers.utils.TableContentImp;
import com.vshmaliukh.webstore.model.Category;
import com.vshmaliukh.webstore.model.Order;
import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.CategoryRepository;
import com.vshmaliukh.webstore.repositories.OrderRepository;
import com.vshmaliukh.webstore.repositories.UserRepository;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.BaseItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.ModelMap;

import java.util.List;


public final class AdminControllerUtils {

    private AdminControllerUtils() {}

    public static TableContentImp<User> generateTableContentForUserView(String keyword, int page, int size,
                                                                        String sortField, String sortDirection,
                                                                        UserRepository repository) {
        TableContentImp<User> tableContent = new TableContentImp<User>(keyword, page, size, sortField, sortDirection) {
            @Override
            public Page<User> formPageIfKeywordIsBlank(Pageable pageable) {
                return repository.findAll(pageable);
            }

            @Override
            public Page<User> formPageWithContentByKeyword(String keyword, Pageable pageable) {
                return repository.findByUsernameIgnoreCase(keyword, pageable);
            }
        };
        return tableContent;
    }

    public static TableContentImp<Category> generateTableContentForCategoryView(String keyword, int page, int size,
                                                                                String sortField, String sortDirection,
                                                                                CategoryRepository repository) {
        TableContentImp<Category> tableContent = new TableContentImp<Category>(keyword, page, size, sortField, sortDirection) {
            @Override
            public Page<Category> formPageIfKeywordIsBlank(Pageable pageable) {
                return repository.findAll(pageable);
            }

            @Override
            public Page<Category> formPageWithContentByKeyword(String keyword, Pageable pageable) {
                return repository.findByNameContainingIgnoreCase(keyword, pageable);
            }
        };
        return tableContent;
    }

    public static TableContentImp<Order> generateTableContentForOrderView(String keyword, int page, int size,
                                                                          String sortField, String sortDirection,
                                                                          OrderRepository repository) {
        TableContentImp<Order> tableContent = new TableContentImp<Order>(keyword, page, size, sortField, sortDirection) {
            @Override
            public Page<Order> formPageIfKeywordIsBlank(Pageable pageable) {
                return repository.findAll(pageable);
            }

            @Override
            public Page<Order> formPageWithContentByKeyword(String keyword, Pageable pageable) {
                return repository.findByStatusContainingIgnoreCase(keyword, pageable);
            }
        };
        return tableContent;
    }

    public static <T extends Item> TableContentImp<T> generateTableContentForItemView(String keyword, int page, int size,
                                                                                      String sortField, String sortDirection,
                                                                                      BaseItemRepository<T> repository) {
        TableContentImp<T> tableContent = new TableContentImp<T>(keyword, page, size, sortField, sortDirection) {
            @Override
            public Page<T> formPageIfKeywordIsBlank(Pageable pageable) {
                return repository.findAll(pageable);
            }

            @Override
            public Page<T> formPageWithContentByKeyword(String keyword, Pageable pageable) {
                return repository.findByNameContainingIgnoreCase(keyword, pageable);
            }
        };
        return tableContent;
    }

    public static void addTableContentWithItems(String keyword,
                                                int page, int size,
                                                String sortField, String sortDirection,
                                                String itemType, ModelMap modelMap, BaseItemRepository itemRepository) {
        TableContentImp<? extends Item> tableContent
                = generateTableContentForItemView(keyword, page, size, sortField, sortDirection, itemRepository);
        List<? extends Item> itemList = tableContent.readContentList();
        ModelMap contentModelMap = tableContent.readContentModelMap();

        modelMap.addAllAttributes(contentModelMap);
        modelMap.addAttribute("itemType", itemType.toLowerCase());
        modelMap.addAttribute("itemList", itemList);
    }

}
