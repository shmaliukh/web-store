package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.OrderStatus;
import com.vshmaliukh.webstore.login.LogInProvider;
import com.vshmaliukh.webstore.login.UserRole;
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

    private static Stream<Arguments> providedArgs_calcTotalOrderItemQuantityTest() {
        Order order1 = new Order();
        Order order2 = new Order();
        Order order3 = new Order();
        Order order4 = new Order();
        Order order5 = new Order();
        return Stream.of(
                Arguments.of(null, Collections.emptyList(), 0),
                Arguments.of(order1, Collections.emptyList(), 0),
                Arguments.of(order2, Collections.singletonList(new OrderItem(null, 1, 1, true, null, order2)), 1),
                Arguments.of(order3, Collections.singletonList(new OrderItem(null, 2, 1, false, new Magazine(), order3)), 2),
                Arguments.of(order4, Arrays.asList(
                        new OrderItem(null, 1, 1, true, new Magazine(), order4),
                        new OrderItem(null, 1, 1, true, new Book(), order4),
                        new OrderItem(null, 1, 1, true, new Newspaper(), order4),
                        new OrderItem(null, 1, 1, true, new Comics(), order4)
                ), 4),
                Arguments.of(order5, Arrays.asList(
                        new OrderItem(null, 2, 5, true, new Magazine(), order5),
                        new OrderItem(null, 3, 4, false, new Book(), order5),
                        new OrderItem(null, 4, 3, false, new Newspaper(), order5),
                        new OrderItem(null, 5, 2, false, new Comics(), order5)
                ), 14)
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_calcTotalOrderItemQuantityTest")
    void calcTotalOrderItemQuantityTest(Order order, List<OrderItem> repositoryOrderItemList, int expectedQuantity) {
        Mockito
                .when(orderItemService.readOrderItemListByOrder(order))
                .thenReturn(repositoryOrderItemList);
        int itemQuantity = orderService.calcTotalOrderItemQuantity(order);

        assertEquals(expectedQuantity, itemQuantity);
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

    private static Stream<Arguments> providedArgs_createEmptyOrderTest() {
        return Stream.of(
                Arguments.of(1L, "some status", ""),
                Arguments.of(2L, "s", ""),
                Arguments.of(3L, "s", "some comment"),
                Arguments.of(4L, "s", null)
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_createEmptyOrderTest")
    void createEmptyOrderTest(Long userId, String status, String comment) {
        User user = new User(userId, "some name", "some@mail.com", LogInProvider.LOCAL, UserRole.ADMIN, "1234", true);
        Mockito
                .when(userService.readUserById(userId))
                .thenReturn(Optional.of(user));
        Optional<Order> optionalOrder = orderService.createEmptyOrder(userId, status, comment);

        assertTrue(optionalOrder.isPresent());
        Order order = optionalOrder.get();
        assertNull(order.getId());
        assertNotNull(order.getOrderItemList());
        assertTrue(order.getOrderItemList().isEmpty());
        assertNotNull(order.getUser());
        assertEquals(userId, order.getUser().getId());
        assertEquals(comment, order.getComment());
        assertEquals(status, order.getStatus());
    }

    @ParameterizedTest
    @MethodSource("providedArgs_createEmptyOrderTest")
    void createEmptyOrderTest_notFoundUser(Long userId, String status, String comment) {
        Mockito
                .when(userService.readUserById(userId))
                .thenReturn(Optional.empty());
        Optional<Order> optionalOrder = orderService.createEmptyOrder(userId, status, comment);

        assertNotNull(optionalOrder);
        assertFalse(optionalOrder.isPresent());
    }

    @Test
    void createEmptyOrderTest_notFoundUserLogErr(CapturedOutput output) {
        Long id = 1L;
        Mockito
                .when(userService.readUserById(id))
                .thenReturn(Optional.empty());
        Optional<Order> optionalOrder = orderService.createEmptyOrder(id, "some status", "some comment");

        assertNotNull(optionalOrder);
        assertFalse(optionalOrder.isPresent());
        assertTrue(output.getOut().contains("problem to created empty order"));
        assertTrue(output.getOut().contains("user not found"));
    }

    @Test
    void createEmptyOrderTest_statusIsBlankLogErr(CapturedOutput output) {
        Long id = 1L;
        Mockito
                .when(userService.readUserById(id))
                .thenReturn(Optional.of(new User(id, "some name", "some@mail.com", LogInProvider.LOCAL, UserRole.ADMIN, "1234", true)));
        Optional<Order> optionalOrder = orderService.createEmptyOrder(id, "", "some comment");

        assertNotNull(optionalOrder);
        assertFalse(optionalOrder.isPresent());
        assertTrue(output.getOut().contains("problem to created empty order"));
        assertTrue(output.getOut().contains("status is blank"));
    }

    private static Stream<Arguments> providedArgs_createEmptyOrderTest_invalidUserId() {
        return Stream.of(
                Arguments.of(null, "some status", ""),
                Arguments.of(0L, "some status", ""),
                Arguments.of(-1L, "s", "")
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_createEmptyOrderTest_invalidUserId")
    void createEmptyOrderTest_invalidUserId(Long userId, String status, String comment) {
        Optional<Order> optionalOrder = orderService.createEmptyOrder(userId, status, comment);

        assertNotNull(optionalOrder);
        assertFalse(optionalOrder.isPresent());
    }

    @Test
    void setUpSoldQuantityIfOrderIsCompletedTest_invalidOrderLogErr(CapturedOutput output) {
        orderService.setUpSoldQuantityIfOrderIsCompleted(null);

        assertTrue(output.getOut().contains("problem to set up sold out quantity if order is completed"));
        assertTrue(output.getOut().contains("invalid order"));
    }

    @Test
    void setUpSoldQuantityIfOrderIsCompletedTest_successfullyChangedLogInfo(CapturedOutput output) {
        Order order = new Order();
        order.setStatus(OrderStatus.Completed.getStatus().getStatusName());
        Mockito
                .when(orderItemService.readOrderItemListByOrder(order))
                .thenReturn(Collections.emptyList());
        orderService.setUpSoldQuantityIfOrderIsCompleted(order);

        assertTrue(output.getOut().contains("successfully save order items as completed"));
    }

    private static Stream<Arguments> providedArgs_setUpSoldQuantityIfOrderIsCompletedTest() {
        Order order = new Order();
        order.setStatus(OrderStatus.Completed.getStatus().getStatusName());

        Book book = new Book(1, "book name", 1, 1, 1, 1, "some description", "some status", true, 0, 123, "some author", new Date());
        Magazine magazine = new Magazine(2, "magazine name", 2, 2, 2, 2, "some description", "some status", true, 0, 345);
        Newspaper newspaper = new Newspaper(3, "newspaper name", 3, 3, 3, 3, "some description", "some status", true, 0, 456);
        Comics comics = new Comics(4, "newspaper name", 4, 4, 4, 4, "some description", "some status", true, 0, 567, "some publisher");
        Book book2 = new Book(1, "book name", 1, 1, 1, 1, "some description", "some status", true, 0, 123, "some author", new Date());
        Magazine magazine2 = new Magazine(2, "magazine name", 2, 2, 2, 2, "some description", "some status", true, 0, 345);
        Newspaper newspaper2 = new Newspaper(3, "newspaper name", 3, 3, 3, 3, "some description", "some status", true, 0, 456);
        Comics comics2 = new Comics(4, "newspaper name", 4, 4, 4, 4, "some description", "some status", true, 0, 567, "some publisher");
        Comics comics3 = new Comics(4, "newspaper name", 4, 4, 4, 4, "some description", "some status", true, 5, 567, "some publisher");
        Comics comics4 = new Comics(4, "newspaper name", 4, 4, 4, 4, "some description", "some status", true, 5, 567, "some publisher");

        OrderItem orderItem1 = new OrderItem(null, 1, 3, true, book, order);
        OrderItem orderItem2 = new OrderItem(null, 2, 3, true, magazine, order);
        OrderItem orderItem3 = new OrderItem(null, 3, 3, true, newspaper, order);
        OrderItem orderItem4 = new OrderItem(null, 4, 3, true, comics, order);
        OrderItem orderItem5 = new OrderItem(null, 5, 3, true, book2, order);
        OrderItem orderItem6 = new OrderItem(null, 6, 3, true, magazine2, order);
        OrderItem orderItem7 = new OrderItem(null, 7, 3, true, newspaper2, order);
        OrderItem orderItem8 = new OrderItem(null, 8, 3, true, comics2, order);
        OrderItem orderItem9 = new OrderItem(null, 4, 3, true, comics3, order);
        OrderItem orderItem10 = new OrderItem(null, 5, 3, true, comics4, order);

        return Stream.of(
                Arguments.of(order, Collections.singletonList(orderItem1), Collections.singletonList(1L)),
                Arguments.of(order, Collections.singletonList(orderItem2), Collections.singletonList(2L)),
                Arguments.of(order, Collections.singletonList(orderItem3), Collections.singletonList(3L)),
                Arguments.of(order, Collections.singletonList(orderItem4), Collections.singletonList(4L)),
                Arguments.of(order, Arrays.asList(orderItem5, orderItem6), Arrays.asList(5L, 6L)),
                Arguments.of(order, Arrays.asList(orderItem7, orderItem8, orderItem9, orderItem10), Arrays.asList(7L, 8L, 9L, 10L))
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_setUpSoldQuantityIfOrderIsCompletedTest")
    void setUpSoldQuantityIfOrderIsCompletedTest(Order order, List<OrderItem> repositoryOrderItemList, List<Long> expectedResult) {
        // TODO add test variants
        Mockito
                .when(orderItemService.readOrderItemListByOrder(order))
                .thenReturn(repositoryOrderItemList);
        orderService.setUpSoldQuantityIfOrderIsCompleted(order);

        for (int i = 0; i < repositoryOrderItemList.size(); i++) {
            int soldOutQuantity = repositoryOrderItemList.get(i).getItem().getSoldOutQuantity();
            assertEquals(expectedResult.get(i), soldOutQuantity);
        }
    }

}