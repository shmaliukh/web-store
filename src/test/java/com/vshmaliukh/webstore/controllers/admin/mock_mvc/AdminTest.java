package com.vshmaliukh.webstore.controllers.admin.mock_mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminTest {

    @Autowired
    private MockMvc mockMvc;

//    @Test
//    public void redirectToAdminHomePageTest() throws Exception {
//        this.mockMvc
//                .perform(get("/admin"))
//                .andDo(print())
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/admin/home"));
//    }

}
