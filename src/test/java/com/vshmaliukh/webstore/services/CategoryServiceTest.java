package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Category;
import com.vshmaliukh.webstore.model.Image;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Book;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Comics;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Magazine;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Newspaper;
import com.vshmaliukh.webstore.repositories.CategoryRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceTest {

    //TODO refactor values

    static final Category category = new Category(1, "some category name", "some description", false, true, null, Collections.EMPTY_SET);
    static final Category category2 = new Category(2, "some category name2", "some description2", false, true, null, Collections.EMPTY_SET);
    static final Category category3 = new Category(3, "some category name3", "some description3", true, true, null, Collections.EMPTY_SET);

    static final Book book = new Book(1, "book name", 1, 1, 1, 1, "some description", "some status", true, 0, 123, "some author", new Date());
    static final Magazine magazine = new Magazine(2, "magazine name", 2, 2, 2, 2, "some description", "some status", true, 0, 345);
    static final Newspaper newspaper = new Newspaper(3, "newspaper name", 3, 3, 3, 3, "some description", "some status", true, 0, 456);
    static final Comics comics = new Comics(4, "newspaper name", 4, 4, 4, 4, "some description", "some status", true, 0, 567, "some publisher");

    static final Category categoryWithOneBook = new Category(1, "some category name1", "some description1", true, true, null, Collections.singleton(book));
    static final Category categoryWithOneMagazine = new Category(2, "some category name2", "some description2", true, true, null, Collections.singleton(magazine));
    static final Category categoryWithNewspaperAndComics = new Category(3, "some category name3", "some description3", true, true, null, new HashSet<>(Arrays.asList(newspaper, comics)));

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
        Optional<Category> optionalCategory
                = categoryService.updateCategory(name, description, isDeleted, isActivated, categoryToUpdate);

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
        Optional<Category> optionalCategory
                = categoryService.updateCategory(name, description, isDeleted, isActivated, categoryToUpdate);

        assertNotNull(optionalCategory);
        assertTrue(optionalCategory.isPresent());
//        FIXME
        assertEquals(expected, optionalCategory.get());
    }

    private static Stream<Arguments> providedArgs_readCategoryByIdTest_failToFind() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(0),
                Arguments.of(-1)
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_readCategoryByIdTest_failToFind")
    void readCategoryByIdTest_failToFind(Integer id) {
        Mockito
                .when(categoryRepository.findById(id))
                .thenReturn(Optional.empty());
        Optional<Category> optionalCategory = categoryService.readCategoryById(id);

        assertNotNull(optionalCategory);
        assertFalse(optionalCategory.isPresent());
    }

    private static Stream<Arguments> providedArgs_readCategoryByIdTest() {
        return Stream.of(
                Arguments.of(1, Optional.of(category)),
                Arguments.of(2, Optional.of(category2)),
                Arguments.of(3, Optional.of(category3)),
                Arguments.of(new Integer(4), Optional.of(category3)),
                Arguments.of(5, Optional.empty())
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_readCategoryByIdTest")
    void readCategoryByIdTest(Integer id, Optional<Category> optionalCategoryToFind) {
        Mockito
                .when(categoryRepository.findById(id))
                .thenReturn(optionalCategoryToFind);
        Optional<Category> optionalCategory = categoryService.readCategoryById(id);

        assertNotNull(optionalCategory);
        assertEquals(optionalCategoryToFind, optionalCategory);
    }

    private static Stream<Arguments> providedArgs_addItemToCategoryTest() {
        return Stream.of(
                Arguments.of(book, category),
                Arguments.of(magazine, category),
                Arguments.of(newspaper, category),
                Arguments.of(comics, category),
                Arguments.of(book, category2),
                Arguments.of(magazine, category2),
                Arguments.of(newspaper, category2),
                Arguments.of(newspaper, categoryWithOneBook),
                Arguments.of(magazine, categoryWithOneBook),
                Arguments.of(comics, categoryWithOneBook),
                Arguments.of(newspaper, categoryWithOneMagazine),
                Arguments.of(book, categoryWithOneMagazine),
                Arguments.of(comics, categoryWithOneMagazine),
                Arguments.of(book, categoryWithNewspaperAndComics),
                Arguments.of(magazine, categoryWithNewspaperAndComics)
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_addItemToCategoryTest")
    void addItemToCategoryTest(Item item, Category category) {
        int oldItemSetSize = category.getItemSet().size();
        categoryService.addItemToCategory(item, category);
        Set<Item> categoryItemSet = category.getItemSet();

        assertNotNull(category);
        assertNotNull(categoryItemSet);
        assertTrue(categoryItemSet.contains(item));
        assertEquals(oldItemSetSize + 1, categoryItemSet.size());
    }

    private static Stream<Arguments> providedArgs_addItemToCategoryTest_ifContainsItem() {
        return Stream.of(
                Arguments.of(book, categoryWithOneBook),
                Arguments.of(magazine, categoryWithOneMagazine),
                Arguments.of(newspaper, categoryWithNewspaperAndComics),
                Arguments.of(comics, categoryWithNewspaperAndComics)
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_addItemToCategoryTest_ifContainsItem")
    void addItemToCategoryTest_ifContainsItem(Item item, Category category) {
        int oldItemSetSize = category.getItemSet().size();
        categoryService.addItemToCategory(item, category);
        Set<Item> categoryItemSet = category.getItemSet();

        assertNotNull(category);
        assertNotNull(categoryItemSet);
        assertTrue(categoryItemSet.contains(item));
        assertEquals(oldItemSetSize, categoryItemSet.size());
    }

    private static Stream<Arguments> providedArgs_addItemToCategoryTest_withoutChangingItemSet() {
        return Stream.of(
                Arguments.of(null, category),
                Arguments.of(null, category2),
                Arguments.of(null, categoryWithOneBook)
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_addItemToCategoryTest_withoutChangingItemSet")
    void addItemToCategoryTest_withoutChangingItemSet(Item item, Category category) {
        int oldItemSetSize = category.getItemSet().size();
        categoryService.addItemToCategory(item, category);
        Set<Item> categoryItemSet = category.getItemSet();

        assertNotNull(category);
        assertNotNull(categoryItemSet);
        assertEquals(oldItemSetSize, categoryItemSet.size());
    }

    public static Stream<Arguments> providedArgs_removeItemFromCategoryTest() {
        return Stream.of(
                Arguments.of(null, categoryWithOneBook, 1),
                Arguments.of(null, categoryWithOneMagazine, 1),
                Arguments.of(null, categoryWithNewspaperAndComics, 2),
                Arguments.of(comics, categoryWithOneBook, 1),
                Arguments.of(magazine, categoryWithOneBook, 1),
                Arguments.of(book, categoryWithOneMagazine, 1),
                Arguments.of(comics, categoryWithOneMagazine, 1),
                Arguments.of(book, categoryWithNewspaperAndComics, 2),
                Arguments.of(magazine, categoryWithNewspaperAndComics, 2),
                Arguments.of(book, categoryWithOneBook, 0),
                Arguments.of(magazine, categoryWithOneMagazine, 0),
                Arguments.of(newspaper, categoryWithNewspaperAndComics, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_removeItemFromCategoryTest")
    void removeItemFromCategoryTest(Item item, Category category, int expectedItemSetSize) {
        categoryService.removeItemFromCategory(item, category);
        Set<Item> categoryItemSet = category.getItemSet();

        assertNotNull(category);
        assertNotNull(categoryItemSet);
        assertEquals(expectedItemSetSize, categoryItemSet.size());
    }


    public static Stream<Arguments> providedArgs_addImageToCategoryTest() {
        byte[] imageData = new byte[]{};
        Image image = new Image(null, "some image name", "some type", imageData);
        Image image2 = new Image(9L, "some image name2", "some type2", imageData);
        Image image3 = new Image(null, "some image name3", "some type3", imageData);
        Image image4 = new Image(null, null, null, null);
        return Stream.of(
                Arguments.of(null, Optional.of(new Image()), category),
                Arguments.of(null, Optional.of(new Image()), category2),
                Arguments.of(new Long(1), Optional.of(new Image()), category3),
                Arguments.of(2L, Optional.of(image), category),
                Arguments.of(3L, Optional.of(new Image()), category2),
                Arguments.of(4L, Optional.of(new Image()), category3),
                Arguments.of(5L, Optional.of(image), category),
                Arguments.of(6L, Optional.of(image2), category2),
                Arguments.of(7L, Optional.of(image3), category3),
                Arguments.of(8L, Optional.of(image4), category2),
                Arguments.of(null, Optional.of(image4), category3)
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_addImageToCategoryTest")
    void addImageToCategoryTest(Long imageId, Optional<Image> optionalImage, Category category) {
        categoryService.addImageToCategory(imageId, optionalImage, category);

        assertTrue(optionalImage.isPresent());
        assertNotNull(category.getImage());
        assertEquals(category.getImage(), optionalImage.get());
    }

    @Test
    @Disabled("Not implemented yet")
    void readAll() {
    }

}