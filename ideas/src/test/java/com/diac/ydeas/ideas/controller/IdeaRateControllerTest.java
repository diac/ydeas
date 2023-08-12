package com.diac.ydeas.ideas.controller;

import com.diac.ydeas.domain.enumeration.Rate;
import com.diac.ydeas.domain.model.Idea;
import com.diac.ydeas.domain.model.IdeaRate;
import com.diac.ydeas.domain.model.IdeaRateId;
import com.diac.ydeas.ideas.service.IdeaRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IdeaRateController.class)
@AutoConfigureMockMvc
public class IdeaRateControllerTest {

    private static final String BASE_URL = "/idea_rate";

    private static final String LIKE_URL = BASE_URL + "/%d/like";

    private static final String DISLIKE_URL = BASE_URL + "/%d/dislike";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private IdeaRateService ideaRateService;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void whenLike() throws Exception {
        int id = 1;
        UUID uuid = UUID.randomUUID();
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn(uuid.toString());
        IdeaRateId ideaRateId = IdeaRateId.builder()
                .idea(
                        Idea.builder()
                                .id(id)
                                .build()
                )
                .userUuid(uuid)
                .build();
        IdeaRate ideaRate = IdeaRate.builder()
                .ideaRateId(ideaRateId)
                .rate(Rate.LIKE)
                .build();
        Mockito.when(ideaRateService.add(ideaRate)).thenReturn(ideaRate);
        String requestUrl = String.format(LIKE_URL, id);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(requestUrl)
                .principal(principal);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void whenDislike() throws Exception {
        int id = 1;
        UUID uuid = UUID.randomUUID();
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn(uuid.toString());
        IdeaRateId ideaRateId = IdeaRateId.builder()
                .idea(
                        Idea.builder()
                                .id(id)
                                .build()
                )
                .userUuid(uuid)
                .build();
        IdeaRate ideaRate = IdeaRate.builder()
                .ideaRateId(ideaRateId)
                .rate(Rate.DISLIKE)
                .build();
        Mockito.when(ideaRateService.add(ideaRate)).thenReturn(ideaRate);
        String requestUrl = String.format(DISLIKE_URL, id);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(requestUrl)
                .principal(principal);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }
}