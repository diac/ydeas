package com.diac.ydeas.ideas.controller;

import com.diac.ydeas.domain.enumeration.IdeaStatus;
import com.diac.ydeas.domain.model.Idea;
import com.diac.ydeas.domain.model.IdeaReview;
import com.diac.ydeas.ideas.service.IdeaReviewService;
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

@WebMvcTest(IdeaReviewController.class)
@AutoConfigureMockMvc
public class IdeaReviewControllerTest {

    private static final String BASE_URL = "/idea_review";

    private static final String APPROVE_URL = BASE_URL + "/approve";

    private static final String DECLINE_URL = BASE_URL + "/decline";

    private static final String IDEA_ID_PARAM_NAME = "idea_id";

    private static final String REVIEWER_USER_ID_PARAM_NAME = "reviewer_user_id";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private IdeaReviewService ideaReviewService;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void whenApprove() throws Exception {
        int id = 1;
        IdeaReview ideaReview = IdeaReview.builder()
                .idea(
                        Idea.builder()
                                .id(id)
                                .build()
                )
                .reviewerUserId(id)
                .ideaStatus(IdeaStatus.APPROVED)
                .build();
        Mockito.when(ideaReviewService.add(ideaReview)).thenReturn(ideaReview);
        mockMvc.perform(
                post(APPROVE_URL)
                        .param(IDEA_ID_PARAM_NAME, String.valueOf(id))
                        .param(REVIEWER_USER_ID_PARAM_NAME, String.valueOf(id))
        ).andExpect(status().isOk());
    }

    @Test
    public void whenDecline() throws Exception {
        int id = 1;
        IdeaReview ideaReview = IdeaReview.builder()
                .idea(
                        Idea.builder()
                                .id(id)
                                .build()
                )
                .reviewerUserId(id)
                .ideaStatus(IdeaStatus.DECLINED)
                .build();
        Mockito.when(ideaReviewService.add(ideaReview)).thenReturn(ideaReview);
        mockMvc.perform(
                post(DECLINE_URL)
                        .param(IDEA_ID_PARAM_NAME, String.valueOf(id))
                        .param(REVIEWER_USER_ID_PARAM_NAME, String.valueOf(id))
        ).andExpect(status().isOk());
    }
}