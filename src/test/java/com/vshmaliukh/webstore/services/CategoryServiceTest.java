package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Category;
import com.vshmaliukh.webstore.model.Image;
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
import java.util.Optional;
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

    private static Stream<Arguments> providedArgs_updateCategoryTest_failToUpdate() {
        return Stream.of(
                Arguments.of("some str1", null, true, true, category),
                Arguments.of(null, "some str2", true, true, category),
                Arguments.of("some str1", "some str2", null, true, category),
                Arguments.of("some str1", "some str2", true, null, category),
                Arguments.of("some str1", "some str2", true, true, null),
                Arguments.of("some str1", "some str2", null, null, category2),
                Arguments.of("", "some str2", false, false, category3),
                Arguments.of("    ", "some str2", false, false, category3),
                Arguments.of(" ", "some str2", false, false, category3),
                Arguments.of(null, null, null, null, category3),
                Arguments.of("some str1", " ", false, false, category3)
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_updateCategoryTest_failToUpdate")
    void updateCategoryTest_failToUpdate(String name, String description, Boolean isDeleted, Boolean isActivated, Category categoryToUpdate) {
        Optional<Category> optionalCategory = categoryService.updateCategory(name, description, isDeleted, isActivated, categoryToUpdate);
        assertNotNull(optionalCategory);
        assertFalse(optionalCategory.isPresent());
    }

    private static Stream<Arguments> providedArgs_updateCategoryTest() {
        return Stream.of(
                Arguments.of(category2.getName(), category2.getDescription(), category2.isDeleted(), category2.isActivated(), category,
                        new Category(category.getId(), category2.getName(), category2.getDescription(), category2.isDeleted(), category2.isActivated(), null, Collections.EMPTY_SET)),
                Arguments.of(category3.getName(), category3.getDescription(), category3.isDeleted(), category3.isActivated(), category,
                        new Category(category.getId(), category3.getName(), category3.getDescription(), category3.isDeleted(), category3.isActivated(), null, Collections.EMPTY_SET)),
                Arguments.of("some name", category3.getDescription(), category2.isDeleted(), category.isActivated(), category2,
                        new Category(category2.getId(), "some name", category3.getDescription(), category2.isDeleted(), category.isActivated(), null, Collections.EMPTY_SET))
                );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_updateCategoryTest")
    void updateCategoryTest_failToUpdate(String name, String description, Boolean isDeleted, Boolean isActivated, Category categoryToUpdate, Category expected) {
        Optional<Category> optionalCategory = categoryService.updateCategory(name, description, isDeleted, isActivated, categoryToUpdate);
        assertNotNull(optionalCategory);
        assertTrue(optionalCategory.isPresent());
        assertEquals(expected, optionalCategory.get());
    }

    @Test
    @Disabled("Not implemented yet")
    void readAll() {
    }

}