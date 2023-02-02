package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Order;
import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.model.items.OrderItem;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Book;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Comics;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Magazine;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Newspaper;
import com.vshmaliukh.webstore.repositories.OrderRepository;
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
class OrderServiceTest {

    @MockBean
    ItemService itemService;

    @MockBean
    UserService userService;

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    OrderItemService orderItemService;

    @Autowired
    OrderService orderService;

    private static Stream<Arguments> providedArgs_readOrderByIdTest() {
        return Stream.of(
                Arguments.of(1L, new Order()),
                Arguments.of(2L, new Order(1L, new User(), new Date(), "some status", "", Collections.emptyList())),
                Arguments.of(100L, new Order(23313L, new User(), new Date(), "some status2", "", Collections.emptyList()))
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_readOrderByIdTest")
    void readOrderByIdTest(Long id, Order repositoryOrder) {
        Mockito
                .when(orderRepository.findById(id))
                .thenReturn(Optional.of(repositoryOrder));
        Optional<Order> optionalOrder = orderService.readOrderById(id);

        assertNotNull(optionalOrder);
        assertTrue(optionalOrder.isPresent());
        Order order = optionalOrder.get();
        assertEquals(repositoryOrder, order);
    }

    @Test
    void readOrderByIdTest_0(CapturedOutput output) {
        Optional<Order> optionalOrder = orderService.readOrderById(0L);

        assertNotNull(optionalOrder);
        assertFalse(optionalOrder.isPresent());
        assertTrue(output.getOut().contains("problem to read order by id"));
        assertTrue(output.getOut().contains("id < 1"));
    }

    @Test
    void readOrderByIdTest_null(CapturedOutput output) {
        Optional<Order> optionalOrder = orderService.readOrderById(null);

        assertNotNull(optionalOrder);
        assertFalse(optionalOrder.isPresent());
        assertTrue(output.getOut().contains("problem to read order by id"));
        assertTrue(output.getOut().contains("id is NULL"));
    }

    private static Stream<Arguments> providedArgs_readOrderItemListByOrderIdTest() {
        return Stream.of(
                Arguments.of(1L, Collections.emptyList()),
                Arguments.of(2L, Collections.singletonList(new OrderItem())),
                Arguments.of(100L, Arrays.asList(new OrderItem(), new OrderItem(), new OrderItem()))
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_readOrderItemListByOrderIdTest")
    void readOrderItemListByOrderIdTest(Long id, List<OrderItem> repositoryOrderItemList) {
        Order order = new Order();
        Mockito
                .when(orderRepository.findById(id))
                .thenReturn(Optional.of(order));
        Mockito
                .when(orderItemService.readOrderItemListByOrder(order))
                .thenReturn(repositoryOrderItemList);

        List<OrderItem> orderItemList = orderService.readOrderItemListByOrderId(id);
        assertNotNull(orderItemList);
        assertEquals(repositoryOrderItemList, orderItemList);
        assertTrue(isUnmodifiableList(orderItemList));
    }

    @Test
    void readOrderItemListByOrderIdTest_0(CapturedOutput output) {
        List<OrderItem> orderItemList = orderService.readOrderItemListByOrderId(0L);

        assertNotNull(orderItemList);
        assertTrue(orderItemList.isEmpty());
        assertTrue(output.getOut().contains("problem to read order item list by order id"));
        assertTrue(output.getOut().contains("id < 1"));
    }

    @Test
    void readOrderItemListByOrderIdTest_null(CapturedOutput output) {
        List<OrderItem> orderItemList = orderService.readOrderItemListByOrderId(null);

        assertNotNull(orderItemList);
        assertTrue(orderItemList.isEmpty());
        assertTrue(output.getOut().contains("problem to read order item list by order id"));
        assertTrue(output.getOut().contains("id is NULL"));
    }

    @Test
    void readOrderItemByIdTest_0(CapturedOutput output) {
        Optional<OrderItem> optionalOrderItem = orderService.readOrderItemById(0L);

        assertNotNull(optionalOrderItem);
        assertFalse(optionalOrderItem.isPresent());
        assertTrue(output.getOut().contains("problem to read order item"));
        assertTrue(output.getOut().contains("id < 1"));
    }

    @Test
    void readOrderItemByIdTest_null(CapturedOutput output) {
        Optional<OrderItem> optionalOrderItem = orderService.readOrderItemById(null);

        assertNotNull(optionalOrderItem);
        assertFalse(optionalOrderItem.isPresent());
        assertTrue(output.getOut().contains("problem to read order item"));
        assertTrue(output.getOut().contains("id is NULL"));
    }

    private static Stream<Arguments> providedArgs_calcOrderTotalSumTest() {
        Order order0 = new Order();
        Order order1 = new Order();
        Order order2 = new Order();
        Order order3 = new Order();
        Order order4 = new Order();
        Order order5 = new Order();
        Order order6 = new Order();
        Order order7 = new Order();
        Order order8 = new Order();
        order0.setOrderItemList(Collections.emptyList());
        order1.setOrderItemList(Collections.singletonList(new OrderItem(null, 2, 3, true, new Magazine(), order1)));
        order2.setOrderItemList(Collections.singletonList(new OrderItem(null, 2, 3, false, new Magazine(), order2)));
        order3.setOrderItemList(Collections.singletonList(new OrderItem(null, 1, 21, true, new Book(), order3)));
        order4.setOrderItemList(Collections.singletonList(new OrderItem(null, 11, 2, true, new Book(), order4)));
        order5.setOrderItemList(Collections.singletonList(new OrderItem(null, 23, 1, true, new Comics(), order5)));
        order6.setOrderItemList(Arrays.asList(
                new OrderItem(null, 2, 3, true, new Magazine(), order6),
                new OrderItem(null, 2, 3, true, new Book(), order6),
                new OrderItem(null, 2, 3, true, new Newspaper(), order6),
                new OrderItem(null, 2, 3, true, new Comics(), order6)));
        order7.setOrderItemList(Arrays.asList(
                new OrderItem(null, 2, 3, true, new Magazine(), order7),
                new OrderItem(null, 2, 3, true, new Magazine(), order7),
                new OrderItem(null, 3, 2, true, new Magazine(), order7),
                new OrderItem(null, 3, 2, true, new Comics(), order7),
                new OrderItem(null, 1, 1, true, new Book(), order7)));
        order8.setOrderItemList(Arrays.asList(
                new OrderItem(null, 5, 4, true, new Magazine(), order8),
                new OrderItem(null, 3, 3, false, new Magazine(), order8),
                new OrderItem(null, 2, 3, true, new Book(), order8),
                new OrderItem(null, 3, 3, false, new Book(), order8)));
        return Stream.of(
                Arguments.of(null, 0),
                Arguments.of(null, 0),
                Arguments.of(order0, 0),
                Arguments.of(order1, 6),
                Arguments.of(order2, 0),
                Arguments.of(order3, 21),
                Arguments.of(order4, 22),
                Arguments.of(order5, 23),
                Arguments.of(order6, 24),
                Arguments.of(order7, 25),
                Arguments.of(order8, 26)
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_calcOrderTotalSumTest")
    void calcOrderTotalSumTest(Order order, int expectedSum) {
        int orderTotalSum = orderService.calcOrderTotalSum(order);

        assertEquals(expectedSum, orderTotalSum);
    }

    @Test
    void calcOrderTotalSumTest_notFoundOrder(CapturedOutput output) {
        Order order = new Order();
        order.setOrderItemList(null);
        int orderTotalSum = orderService.calcOrderTotalSum(order);

        assertEquals(0, orderTotalSum);
        assertTrue(output.getOut().contains("problem to calculate order total sum"));
        assertTrue(output.getOut().contains("order item list is NULL"));
    }

    @Test
    void calcOrderTotalSumTest_orderIsNull(CapturedOutput output) {
        int orderTotalSum = orderService.calcOrderTotalSum(null);

        assertEquals(0, orderTotalSum);
        assertTrue(output.getOut().contains("problem to calculate order total sum"));
        assertTrue(output.getOut().contains("invalid order"));
    }


    @Test
    void calcTotalOrderItemQuantityTest_null(CapturedOutput output) {
        int itemQuantity = orderService.calcTotalOrderItemQuantity(null);

        assertTrue(output.getOut().contains("problem to calc order items quantity"));
        assertTrue(output.getOut().contains("invalid order"));
        assertEquals(0, itemQuantity);
    }

    @Test
    void saveTest(CapturedOutput output) {
        orderService.save(new Order(1L, new User(), new Date(), "some status", "", Collections.emptyList()));

        assertTrue(output.getOut().contains("saved order"));
    }

    @Test
    void saveTest_null(CapturedOutput output) {
        orderService.save(null);

        assertTrue(output.getOut().contains("problem to save order"));
    }

    private static Stream<Arguments> providedArgs_findUserOrderListTest() {
        return Stream.of(
                Arguments.of(Collections.emptyList()),
                Arguments.of(Collections.singletonList(new Order())),
                Arguments.of(Arrays.asList(new Order(), new Order(), new Order()))
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_findUserOrderListTest")
    void findUserOrderListTest(List<Order> repositoryOrderList) {
        User user = new User();
        Mockito
                .when(orderRepository.findAllByUser(user))
                .thenReturn(repositoryOrderList);
        List<Order> userOrderList = orderService.findUserOrderList(user);

        assertNotNull(userOrderList);
        assertEquals(repositoryOrderList, userOrderList);
        assertTrue(isUnmodifiableList(userOrderList));
    }

}