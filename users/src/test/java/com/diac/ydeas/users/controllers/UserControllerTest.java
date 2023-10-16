package com.diac.ydeas.users.controllers;

import com.diac.ydeas.domain.model.User;
import com.diac.ydeas.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserControllerImpl.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private static final String BASE_URL = "";

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void whenIndex() throws Exception {
        Mockito.when(userService.findAll())
                .thenReturn(
                        List.of(
                                User.builder()
                                        .uuid(UUID.randomUUID())
                                        .build()
                        )
                );
        mockMvc.perform(
                get(BASE_URL)
        ).andExpect(status().isOk());
    }

    @Test
    public void whenFindByUuid() throws Exception {
        UUID uuid = UUID.randomUUID();
        Mockito.when(userService.findByUuid(uuid)).thenReturn(
                User.builder()
                        .uuid(uuid)
                        .build()
        );
        String requestUrl = String.format("%s/%s", BASE_URL, uuid);
        mockMvc.perform(
                get(requestUrl)
        ).andExpect(status().isOk());
    }
}