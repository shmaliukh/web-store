package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Book;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Comics;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Magazine;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Newspaper;
import com.vshmaliukh.webstore.repositories.ItemRepositoryProvider;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.util.*;
import java.util.stream.Stream;

import static com.vshmaliukh.webstore.TestUtils.isUnmodifiableList;
import static com.vshmaliukh.webstore.TestUtils.isUnmodifiableSet;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class ItemServiceTest {

    @MockBean
    ImageService imageService;

    @MockBean
    ItemRepository itemRepository;

    @MockBean
    ItemRepositoryProvider itemRepositoryProvider;

    @Autowired
    ItemService itemService;

    private static Stream<Arguments> providedArgs_readTypeNameTest() {
        return Stream.of(
                Arguments.of(Collections.emptySet()),
                Arguments.of(Collections.singleton("Book")),
                Arguments.of(Collections.singleton("Newspaper")),
                Arguments.of(Collections.singleton(Book.class.getSimpleName())),
                Arguments.of(new HashSet<>(Arrays.asList("Book", "Magazine"))),
                Arguments.of(new HashSet<>(Arrays.asList(Book.class.getSimpleName(), Magazine.class.getSimpleName())))
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_readTypeNameTest")
    void readTypeNameTest(Set<String> repositoryTypeNameSet) {
        Mockito
                .when(itemRepositoryProvider.readTypeNameSet())
                .thenReturn(repositoryTypeNameSet);
        Set<String> typeNameSet = itemService.readTypeNameSet();

        assertNotNull(typeNameSet);
        assertEquals(repositoryTypeNameSet, typeNameSet);
        assertTrue(isUnmodifiableSet(typeNameSet));
    }

    @Test
    void deleteItemTest_nullLogErr(CapturedOutput output) {
        itemService.deleteItem(null);

        assertTrue(output.getOut().contains("problem to delete item"));
        assertTrue(output.getOut().contains("invalid item"));
    }

    @Test
    void deleteItemTest_successLogInfo(CapturedOutput output) {
        Book item = new Book(1, "book name", 1, 1, 1, 1, "some description", "some status", true, 0, 123, "some author", new Date());
        itemService.deleteItem(item);

        assertTrue(output.getOut().contains("item successfully deleted"));
    }

    private static Stream<Arguments> providedArgs_readItemByIdTest() {
        Book book = new Book(1, "book name", 1, 1, 1, 1, "some description", "some status", true, 0, 123, "some author", new Date());
        Magazine magazine = new Magazine(2, "magazine name", 2, 2, 2, 2, "some description", "some status", true, 0, 345);
        Newspaper newspaper = new Newspaper(3, "newspaper name", 3, 3, 3, 3, "some description", "some status", true, 0, 456);
        Comics comics = new Comics(4, "newspaper name", 4, 4, 4, 4, "some description", "some status", true, 0, 567, "some publisher");
        return Stream.of(
                Arguments.of(1, book),
                Arguments.of(2, magazine),
                Arguments.of(3, newspaper),
                Arguments.of(4, comics)
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_readItemByIdTest")
    void readItemByIdTest(Integer id, Item expectedItem) {
        Mockito
                .when(itemRepository.findById(id))
                .thenReturn(Optional.of(expectedItem));
        Optional<Item> optionalItem = itemService.readItemById(id);

        assertNotNull(optionalItem);
        assertTrue(optionalItem.isPresent());
        Item item = optionalItem.get();
        assertEquals(expectedItem.getId(), item.getId());
        assertEquals(expectedItem, item);
    }

    private static Stream<Integer> providedArgs_readItemByIdTest_illegalId() {
        return Stream.of(null, 0, -1);
    }

    @ParameterizedTest
    @MethodSource("providedArgs_readItemByIdTest_illegalId")
    void readItemByIdTest_illegalId(Integer id) {
        Optional<Item> optionalItem = itemService.readItemById(id);

        assertNotNull(optionalItem);
        assertFalse(optionalItem.isPresent());
    }

    @Test
    void readItemByIdTest_idIsNullLogErr(CapturedOutput output) {
        Optional<Item> optionalItem = itemService.readItemById(null);

        assertNotNull(optionalItem);
        assertFalse(optionalItem.isPresent());
        assertTrue(output.getOut().contains("problem to read item by id"));
        assertTrue(output.getOut().contains("item id is NULL"));
    }

    @Test
    void readItemByIdTest_idIsLessThan1LogErr(CapturedOutput output) {
        Optional<Item> optionalItem = itemService.readItemById(0);

        assertNotNull(optionalItem);
        assertFalse(optionalItem.isPresent());
        assertTrue(output.getOut().contains("problem to read item by id"));
        assertTrue(output.getOut().contains("item id < 1"));
    }

    @Test
    void saveItemTest_itemIsInvalidLogErr(CapturedOutput output) {
        itemService.saveItem(null);

        assertTrue(output.getOut().contains("problem to save item"));
        assertTrue(output.getOut().contains("invalid item"));
    }

    @Test
    void saveItemTest_successLogInfo(CapturedOutput output) {
        Book item = new Book(1, "book name", 1, 1, 1, 1, "some description", "some status", true, 0, 123, "some author", new Date());
        itemService.saveItem(item);

        assertTrue(output.getOut().contains("item successfully saved"));
    }

    @Test
    void isItemSavedTest_itemIsInvalidLogErr(CapturedOutput output) {
        boolean isSaved = itemService.isItemSaved(null);

        assertFalse(isSaved);
        assertTrue(output.getOut().contains("problem to check if the item is saved"));
        assertTrue(output.getOut().contains("invalid item"));
    }

    @Test
    void isItemSavedTest_idIsNullLogErr(CapturedOutput output) {
        Book item = new Book();
        boolean isSaved = itemService.isItemSaved(item);

        assertFalse(isSaved);
        assertTrue(output.getOut().contains("problem to check if the item is saved"));
        assertTrue(output.getOut().contains("id is NULL"));
    }

    @Test
    void isItemSavedTest_idIsLessThan1LogErr(CapturedOutput output) {
        Book item = new Book(0, "book name", 1, 1, 1, 1, "some description", "some status", true, 0, 123, "some author", new Date());
        boolean isSaved = itemService.isItemSaved(item);

        assertFalse(isSaved);
        assertTrue(output.getOut().contains("problem to check if the item is saved"));
        assertTrue(output.getOut().contains("id < 1"));
    }

    private static Stream<Arguments> providedArgs_isItemSavedTest() {
        Book book_1 = new Book(-1, "book name", 1, 1, 1, 1, "some description", "some status", true, 0, 123, "some author", new Date());
        Book book0 = new Book(0, "book name", 1, 1, 1, 1, "some description", "some status", true, 0, 123, "some author", new Date());
        Magazine magazine0 = new Magazine(0, "magazine name", 2, 2, 2, 2, "some description", "some status", true, 0, 345);
        Newspaper newspaper0 = new Newspaper(0, "newspaper name", 3, 3, 3, 3, "some description", "some status", true, 0, 456);
        Comics comics0 = new Comics(0, "newspaper name", 4, 4, 4, 4, "some description", "some status", true, 0, 567, "some publisher");
        Book book = new Book(1, "book name", 1, 1, 1, 1, "some description", "some status", true, 0, 123, "some author", new Date());
        Magazine magazine = new Magazine(2, "magazine name", 2, 2, 2, 2, "some description", "some status", true, 0, 345);
        Newspaper newspaper = new Newspaper(3, "newspaper name", 3, 3, 3, 3, "some description", "some status", true, 0, 456);
        Comics comics = new Comics(4, "newspaper name", 4, 4, 4, 4, "some description", "some status", true, 0, 567, "some publisher");
        return Stream.of(
                Arguments.of(book, true),
                Arguments.of(magazine, true),
                Arguments.of(newspaper, true),
                Arguments.of(comics, true),
                Arguments.of(book, false),
                Arguments.of(magazine, false),
                Arguments.of(newspaper, false),
                Arguments.of(comics, false),
                Arguments.of(book_1, false),
                Arguments.of(book0, false),
                Arguments.of(magazine0, false),
                Arguments.of(newspaper0, false),
                Arguments.of(comics0, false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_isItemSavedTest")
    void isItemSavedTest(Item item, boolean expectedResult) {
        Mockito
                .when(itemRepository.existsById(item.getId()))
                .thenReturn(expectedResult);
        boolean isSaved = itemService.isItemSaved(item);

        assertEquals(expectedResult, isSaved);
    }

    @Test
    void readAllItemsByTypeNameTest_notFoundRepositoryLogErr(CapturedOutput output) {
        Book item = new Book();
        String itemTypeStr = item.getTypeStr();
        Mockito
                .when(itemRepositoryProvider.getItemRepositoryByItemClassName(itemTypeStr))
                .thenReturn(null);
        List<? extends Item> allItemsByTypeName = itemService.readAllItemsByTypeName(itemTypeStr);

        assertNotNull(allItemsByTypeName);
        assertTrue(allItemsByTypeName.isEmpty());
        assertTrue(output.getOut().contains("problem to read all items by type name"));
    }

    private static Stream<Arguments> providedArgs_readAllItemsByTypeNameTest() {
        Book book = new Book(1, "book name", 1, 1, 1, 1, "some description", "some status", true, 0, 123, "some author", new Date());
        Magazine magazine = new Magazine(2, "magazine name", 2, 2, 2, 2, "some description", "some status", true, 0, 345);
        Newspaper newspaper = new Newspaper(3, "newspaper name", 3, 3, 3, 3, "some description", "some status", true, 0, 456);
        Comics comics = new Comics(4, "newspaper name", 4, 4, 4, 4, "some description", "some status", true, 0, 567, "some publisher");
        return Stream.of(
                Arguments.of(Collections.singletonList(book)),
                Arguments.of(Collections.singletonList(magazine)),
                Arguments.of(Collections.singletonList(newspaper)),
                Arguments.of(Collections.singletonList(comics))
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_readAllItemsByTypeNameTest")
    void readAllItemsByTypeNameTest(List<Item> repositoryItemList) {
        // FIXME solve problem to mock repository by type
        Book item = new Book();
        String itemTypeStr = item.getTypeStr();
        List<? extends Item> allItemsByTypeName = itemService.readAllItemsByTypeName(itemTypeStr);

        assertNotNull(allItemsByTypeName);
//        assertFalse(allItemsByTypeName.isEmpty());
//        assertTrue(isUnmodifiableList(allItemsByTypeName));
//        assertEquals(repositoryItemList, allItemsByTypeName);
    }

}