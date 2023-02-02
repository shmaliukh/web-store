package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Order;
import com.vshmaliukh.webstore.model.User;
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
        assertTrue(Collections.unmodifiableList(userOrderList).getClass().isInstance(Collections.unmodifiableList(new ArrayList<>())));
    }

}