package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Order;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.model.items.OrderItem;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Book;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Comics;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Magazine;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Newspaper;
import com.vshmaliukh.webstore.repositories.OrderItemRepository;
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
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class OrderItemServiceTest {

    @MockBean
    ItemService itemService;

    @MockBean
    OrderItemRepository orderItemRepository;

    @Autowired
    OrderItemService orderItemService;

    @Test
    void getOrderItemRepositoryTest() {
        assertNotNull(orderItemService.getOrderItemRepository());
    }

    private static Stream<Arguments> providedArgs_generateOrderItemIfNotExistTest() {
        Book book = new Book(1, "book name", 1, 1, 1, 1, "some description", "some status", true, 0, 123, "some author", new Date());
        Magazine magazine = new Magazine(2, "magazine name", 2, 2, 2, 2, "some description", "some status", true, 0, 345);
        Newspaper newspaper = new Newspaper(3, "newspaper name", 3, 3, 3, 3, "some description", "some status", true, 0, 456);
        Comics comics = new Comics(4, "newspaper name", 4, 4, 4, 4, "some description", "some status", true, 0, 567, "some publisher");
        Comics comics2 = new Comics(4, "newspaper name", 4, 4, -10, -10, "some description", "some status", true, 0, 567, "some publisher");

        Order emptyOrder = new Order();

        return Stream.of(
                Arguments.of(-1, book, emptyOrder),
                Arguments.of(0, book, emptyOrder),
                Arguments.of(1, book, emptyOrder),
                Arguments.of(2, magazine, emptyOrder),
                Arguments.of(3, newspaper, emptyOrder),
                Arguments.of(4, comics, emptyOrder),
                Arguments.of(5, comics2, emptyOrder)
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_generateOrderItemIfNotExistTest")
    void generateOrderItemIfNotExistTest(Integer quantity, Item item, Order order) {
        OrderItem orderItem = orderItemService.generateOrderItemIfNotExist(quantity, item, order);

        assertNotNull(orderItem);
        assertNull(orderItem.getOrderItemId());
        assertNotNull(orderItem.getItem());
        assertNotNull(orderItem.getOrder());
        assertTrue(orderItem.getQuantity() >= 0);
        assertTrue(orderItem.getOrderItemPrice() >= 0);
    }

    private static Stream<Arguments> providedArgs_readOrderItemsByOrderTest() {
        return Stream.of(
                Arguments.of(Collections.emptyList()),
                Arguments.of(Collections.singletonList(new OrderItem())),
                Arguments.of(Arrays.asList(new OrderItem(), new OrderItem(), new OrderItem()))
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_readOrderItemsByOrderTest")
    void readOrderItemsByOrderTest(List<OrderItem> repositoryOrderItemList) {
        Order order = new Order();
        Mockito
                .when(orderItemRepository.readOrderItemsByOrder(order))
                .thenReturn(repositoryOrderItemList);
        List<OrderItem> orderItemList = orderItemService.readOrderItemListByOrder(order);

        assertNotNull(orderItemList);
        assertEquals(orderItemList, repositoryOrderItemList);
        assertTrue(isUnmodifiableList(orderItemList));
    }

    private static Stream<Arguments> providedArgs_readOrderItemByOrderItemIdTest() {
        return Stream.of(
                Arguments.of(1L, new OrderItem()),
                Arguments.of(2L, new OrderItem()),
                Arguments.of(123_456_789L, new OrderItem())
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_readOrderItemByOrderItemIdTest")
    void readOrderItemByOrderItemIdTest(Long id, OrderItem repositoryOrderItem) {
        Mockito
                .when(orderItemRepository.readOrderItemByOrderItemId(id))
                .thenReturn(Optional.ofNullable(repositoryOrderItem));
        Optional<OrderItem> optionalOrderItem = orderItemService.readOrderItemByOrderItemId(id);

        assertNotNull(optionalOrderItem);
        assertTrue(optionalOrderItem.isPresent());
        assertTrue(orderItemService.isValidEntity(optionalOrderItem.get()));
    }

    private static Stream<Long> providedArgs_readOrderItemByOrderItemIdTest_invalid() {
        return Stream.of(0L, -1L, null);
    }

    @ParameterizedTest
    @MethodSource("providedArgs_readOrderItemByOrderItemIdTest_invalid")
    void readOrderItemByOrderItemIdTest_invalid(Long id) {
        Mockito
                .when(orderItemRepository.readOrderItemByOrderItemId(id))
                .thenReturn(Optional.empty());
        Optional<OrderItem> optionalOrderItem = orderItemService.readOrderItemByOrderItemId(id);

        assertNotNull(optionalOrderItem);
        assertFalse(optionalOrderItem.isPresent());
    }

    @Test
    void saveTest_null(CapturedOutput output) {
        orderItemService.save(null);

        assertTrue(output.getOut().contains("problem to save order item"));
    }

    @Test
    void saveTest_quantityIsLessThan1(CapturedOutput output) {
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(0);
        orderItemService.save(orderItem);

        assertTrue(output.getOut().contains("order item quantity < 1, set active state as 'false'"));
    }

    @Test
    void saveTest(CapturedOutput output) {
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(1);
        orderItemService.save(orderItem);

        assertTrue(output.getOut().contains("saved orderItem"));
    }

    private static Stream<Arguments> providedArgs_generateOrderItemIfExistTest() {
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setQuantity(0);
        orderItem1.setOrder(new Order());
        orderItem1.setItem(new Magazine());
        orderItem1.setPrice(100);
        OrderItem orderItem2 = new OrderItem();
        orderItem2.setQuantity(1);
        orderItem2.setOrder(new Order());
        orderItem2.setItem(new Magazine());
        orderItem2.setPrice(100);
        OrderItem orderItem3 = new OrderItem();
        orderItem3.setQuantity(-1);
        orderItem3.setOrder(new Order());
        orderItem3.setItem(new Book());
        orderItem3.setPrice(100);
        OrderItem orderItem4 = new OrderItem();
        orderItem4.setQuantity(0);
        orderItem4.setOrder(new Order());
        orderItem4.setItem(new Comics());
        orderItem4.setPrice(100);
        OrderItem orderItem5 = new OrderItem();
        orderItem5.setQuantity(5);
        orderItem5.setOrder(new Order());
        orderItem5.setItem(new Newspaper());
        orderItem5.setPrice(100);
        OrderItem orderItem6 = new OrderItem();
        orderItem6.setQuantity(-100);
        orderItem6.setOrder(new Order());
        orderItem6.setItem(new Book());
        orderItem6.setPrice(100);
        return Stream.of(
                Arguments.of(1, orderItem1, 1),
                Arguments.of(1, orderItem2, 2),
                Arguments.of(1, orderItem3, 1),
                Arguments.of(3, orderItem4, 3),
                Arguments.of(5, orderItem5, 10),
                Arguments.of(3, orderItem6, 3)
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_generateOrderItemIfExistTest")
    void generateOrderItemIfExistTest(int quantity, OrderItem orderItem, int expectedQuantity) {
        OrderItem updatedOrderItem = orderItemService.generateOrderItemIfExist(quantity, orderItem);

        assertNotNull(updatedOrderItem);
        assertEquals(updatedOrderItem.getOrderItemId(), orderItem.getOrderItemId());
        assertEquals(expectedQuantity, updatedOrderItem.getQuantity());
        assertEquals(orderItem.getOrderItemPrice(), updatedOrderItem.getOrderItemPrice());
        assertNotNull(updatedOrderItem.getOrder());
        assertNotNull(updatedOrderItem.getItem());
    }

}