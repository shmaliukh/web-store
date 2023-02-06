package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.repositories.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class CartItemServiceTest {

    @MockBean
    CartItemRepository cartItemRepository;

    @Autowired
    CartItemService cartItemService;




}
