package com.vshmaliukh.webstore.controllers.admin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminCategoryControllerTest {

    @Autowired
    AdminCategoryController adminCategoryController;


    @Test
    public void contextLoads() {
        Assertions.assertNotNull(adminCategoryController);
    }

}