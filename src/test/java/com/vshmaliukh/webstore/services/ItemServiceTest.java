package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Image;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Book;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Magazine;
import com.vshmaliukh.webstore.repositories.ItemRepositoryProvider;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.BaseItemRepository;
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
    void deleteItemTest_null(CapturedOutput output) {
        itemService.deleteItem(null);

        assertTrue(output.getOut().contains("problem to delete item"));
        assertTrue(output.getOut().contains("invalid item"));
    }

}