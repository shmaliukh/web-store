package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Category;
import com.vshmaliukh.webstore.repositories.CategoryRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceTest {

    static Category category = new Category(1, "some category name", "some description", false, true, null, Collections.EMPTY_SET);
    static Category category2 = new Category(2, "some category name2", "some description2", false, true, null, Collections.EMPTY_SET);
    static Category category3 = new Category(3, "some category name3", "some description3", true, true, null, Collections.EMPTY_SET);

    @MockBean
    CategoryRepository categoryRepository;

    @Autowired
    CategoryService categoryService;

    @BeforeEach
    void beforeEach() {
        categoryService.setCategoryRepository(categoryRepository);
    }

    private static Stream<Arguments> providedArgs_readAllTest() {
        return Stream.of(
                Arguments.of(Collections.EMPTY_LIST),
                Arguments.of(Collections.singletonList(category)),
                Arguments.of(Collections.singletonList(category2)),
                Arguments.of(Arrays.asList(category, category2)),
                Arguments.of(Arrays.asList(category2, category)),
                Arguments.of(Arrays.asList(category2, category2)),
                Arguments.of(Arrays.asList(category, category2, category3))
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_readAllTest")
    void readAllEmptyTest(List<Category> expectedList) {
        Mockito
                .when(categoryRepository.findAll())
                .thenReturn(expectedList);
        List<Category> actual = categoryService.readAll();
        assertNotNull(actual);
        assertEquals(expectedList, actual);
    }

    private static Stream<Arguments> providedArgs_buildBaseCategoryTest() {
        String someName = "some name";
        String someDescription = "some description";
        return Stream.of(
                // TODO refactor args
                Arguments.of(null, null, new Category(null, null)),
                Arguments.of(null, someDescription, new Category(null, someDescription)),
                Arguments.of(someName, null, new Category(someName, null)),
                Arguments.of(someName, someDescription, new Category(someName, someDescription)),
                Arguments.of(someName, someDescription, new Category(someName, someDescription))
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_buildBaseCategoryTest")
    void buildBaseCategoryTest(String name, String description, Category expectedCategory) {
        Category actualCategory = categoryService.buildBaseCategory(name, description);
        assertEquals(expectedCategory, actualCategory);
    }

    private static Stream<Arguments> providedArgs_readCategoryNameListTest() {
        return Stream.of(
                Arguments.of(Arrays.asList(category, category2, category3), Arrays.asList(category.getName(), category2.getName(), category3.getName())),
                Arguments.of(Arrays.asList(category, category, category), Arrays.asList(category.getName(), category.getName(), category.getName())),
                Arguments.of(Arrays.asList(null, category), Collections.singletonList(category.getName())),
                Arguments.of(Arrays.asList(category, null), Collections.singletonList(category.getName())),
                Arguments.of(Collections.singletonList(category), Collections.singletonList(category.getName())),
                Arguments.of(Collections.singletonList(category2), Collections.singletonList(category2.getName())),
                Arguments.of(Collections.singletonList(null), Collections.emptyList()),
                Arguments.of(Collections.emptyList(), Collections.emptyList())
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_readCategoryNameListTest")
    void readCategoryNameListTest(List<Category> categoryList, List<String> expected) {
        Mockito
                .when(categoryRepository.findAll())
                .thenReturn(categoryList);
        List<String> actual = categoryService.readCategoryNameList();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @Disabled("Not implemented yet")
    void readAll() {
    }

}