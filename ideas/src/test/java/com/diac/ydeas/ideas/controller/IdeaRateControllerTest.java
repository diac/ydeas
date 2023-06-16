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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IdeaRateController.class)
@AutoConfigureMockMvc
public class IdeaRateControllerTest {

    private static final String BASE_URL = "/idea_rate";

    private static final String LIKE_URL = BASE_URL + "/like";

    private static final String DISLIKE_URL = BASE_URL + "/dislike";

    private static final String IDEA_ID_PARAM_NAME = "idea_id";

    private static final String USER_ID_PARAM_NAME = "user_id";

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
        IdeaRateId ideaRateId = IdeaRateId.builder()
                .idea(
                        Idea.builder()
                                .id(id)
                                .build()
                )
                .userId(id)
                .build();
        IdeaRate ideaRate = IdeaRate.builder()
                .ideaRateId(ideaRateId)
                .rate(Rate.LIKE)
                .build();
        Mockito.when(ideaRateService.add(ideaRate)).thenReturn(ideaRate);
        mockMvc.perform(
                post(LIKE_URL)
                        .param(IDEA_ID_PARAM_NAME, String.valueOf(id))
                        .param(USER_ID_PARAM_NAME, String.valueOf(id))
        ).andExpect(status().isOk());
    }

    @Test
    public void whenDislike() throws Exception {
        int id = 1;
        IdeaRateId ideaRateId = IdeaRateId.builder()
                .idea(
                        Idea.builder()
                                .id(id)
                                .build()
                )
                .userId(id)
                .build();
        IdeaRate ideaRate = IdeaRate.builder()
                .ideaRateId(ideaRateId)
                .rate(Rate.DISLIKE)
                .build();
        Mockito.when(ideaRateService.add(ideaRate)).thenReturn(ideaRate);
        mockMvc.perform(
                post(DISLIKE_URL)
                        .param(IDEA_ID_PARAM_NAME, String.valueOf(id))
                        .param(USER_ID_PARAM_NAME, String.valueOf(id))
        ).andExpect(status().isOk());
    }
}