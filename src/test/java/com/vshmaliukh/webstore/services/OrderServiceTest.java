package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Order;
import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.model.items.OrderItem;
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

    private static Stream<Arguments> providedArgs_readOrderItemListByOrderIdTest() {
        return Stream.of(
                Arguments.of(1L, Collections.emptyList()),
                Arguments.of(2L, Collections.singletonList(new OrderItem())),
                Arguments.of(100L, Arrays.asList(new OrderItem(), new OrderItem(), new OrderItem()))
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_readOrderItemListByOrderIdTest")
    void readOrderItemListByOrderIdTest(Long id, List<OrderItem> repositoryOrderItemList){
        Order order = new Order();
        Mockito
                .when( orderRepository.findById(id))
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
    void readOrderItemListByOrderIdTest_0(CapturedOutput output){
        List<OrderItem> orderItemList = orderService.readOrderItemListByOrderId(0L);

        assertNotNull(orderItemList);
        assertTrue(orderItemList.isEmpty());
        assertTrue(output.getOut().contains("problem to read order item list by order id"));
        assertTrue(output.getOut().contains("id < 1"));
    }

    @Test
    void readOrderItemListByOrderIdTest_null(CapturedOutput output){
        List<OrderItem> orderItemList = orderService.readOrderItemListByOrderId(null);

        assertNotNull(orderItemList);
        assertTrue(orderItemList.isEmpty());
        assertTrue(output.getOut().contains("problem to read order item list by order id"));
        assertTrue(output.getOut().contains("id is NULL"));
    }

    @Test
    void readOrderItemByIdTest_0(CapturedOutput output){
        Optional<OrderItem> optionalOrderItem = orderService.readOrderItemById(0L);

        assertNotNull(optionalOrderItem);
        assertFalse(optionalOrderItem.isPresent());
        assertTrue(output.getOut().contains("problem to read order item"));
        assertTrue(output.getOut().contains("id < 1"));
    }

    @Test
    void readOrderItemByIdTest_null(CapturedOutput output){
        Optional<OrderItem> optionalOrderItem = orderService.readOrderItemById(null);

        assertNotNull(optionalOrderItem);
        assertFalse(optionalOrderItem.isPresent());
        assertTrue(output.getOut().contains("problem to read order item"));
        assertTrue(output.getOut().contains("id is NULL"));
    }

    @Test
    void calcTotalOrderItemsTest_null(CapturedOutput output) {
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