package com.diac.ydeas.ideas.controller;

import com.diac.ydeas.domain.exception.ResourceNotFoundException;
import com.diac.ydeas.domain.model.Idea;
import com.diac.ydeas.ideas.service.IdeaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IdeaController.class)
@AutoConfigureMockMvc
public class IdeaControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private IdeaService ideaService;

    private static final String BASE_URL = "/idea";

    private static final ObjectWriter OBJECT_WRITER;

    static {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_WRITER = objectMapper.writer().withDefaultPrettyPrinter();
    }

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void whenIndex() throws Exception {
        mockMvc.perform(
                get(BASE_URL)
        ).andExpect(status().isOk());
    }

    @Test
    public void whenGetFound() throws Exception {
        int id = 1;
        Mockito.when(ideaService.findById(id))
                .thenReturn(
                        Idea.builder()
                                .id(id)
                                .build()
                );
        String requestUrl = String.format("%s/%d", BASE_URL, id);
        mockMvc.perform(
                get(requestUrl)
        ).andExpect(status().isOk());
    }

    @Test
    public void whenGetNotFound() throws Exception {
        int id = 1;
        Mockito.when(ideaService.findById(id))
                .thenThrow(ResourceNotFoundException.class);
        String requestUrl = String.format("%s/%d", BASE_URL, id);
        mockMvc.perform(
                get(requestUrl)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void whenPost() throws Exception {
        int id = 1;
        String value = "test";
        LocalDateTime now = LocalDateTime.now();
        Idea newIdea = Idea.builder()
                .title(value)
                .description(value)
                .authorUserId(id)
                .build();
        Idea savedIdea = Idea.builder()
                .id(id)
                .title(value)
                .description(value)
                .authorUserId(id)
                .createdAt(now)
                .build();
        String requestBody = OBJECT_WRITER.writeValueAsString(newIdea);
        String responseBody = OBJECT_WRITER.writeValueAsString(savedIdea);
        Mockito.when(ideaService.add(newIdea)).thenReturn(savedIdea);
        mockMvc.perform(
                        post(BASE_URL)
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().json(responseBody));
    }

    @Test
    public void whenPostWithNullValuesThenResponseStatusIsBadRequest() throws Exception {
        Idea idea = Idea.builder()
                .build();
        String requestBody = OBJECT_WRITER.writeValueAsString(idea);
        mockMvc.perform(
                post(BASE_URL)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void whenPostWithBlankValuesThenResponseStatusIsBadRequest() throws Exception {
        int id = 1;
        Idea idea = Idea.builder()
                .title("")
                .description("")
                .authorUserId(id)
                .build();
        String requestBody = OBJECT_WRITER.writeValueAsString(idea);
        mockMvc.perform(
                post(BASE_URL)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void whenPut() throws Exception {
        int id = 1;
        String value = "test";
        LocalDateTime now = LocalDateTime.now();
        Idea idea = Idea.builder()
                .id(id)
                .title(value)
                .description(value)
                .authorUserId(id)
                .createdAt(now)
                .build();
        String jsonValue = OBJECT_WRITER.writeValueAsString(idea);
        String requestUrl = String.format("%s/%d", BASE_URL, id);
        Mockito.when(ideaService.update(id, idea))
                .thenReturn(idea);
        mockMvc.perform(
                        put(requestUrl)
                                .content(jsonValue)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(jsonValue));
    }

    @Test
    public void whenPutWithNullValuesThenResponseStatusIsBadRequest() throws Exception {
        int id = 1;
        Idea idea = Idea.builder()
                .id(id)
                .build();
        String jsonValue = OBJECT_WRITER.writeValueAsString(idea);
        String requestUrl = String.format("%s/%d", BASE_URL, id);
        mockMvc.perform(
                put(requestUrl)
                        .content(jsonValue)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void whenPutWithBlankValuesThenResponseStatusIsBadRequest() throws Exception {
        int id = 1;
        Idea idea = Idea.builder()
                .id(id)
                .title("")
                .description("")
                .authorUserId(id)
                .build();
        String jsonValue = OBJECT_WRITER.writeValueAsString(idea);
        String requestUrl = String.format("%s/%d", BASE_URL, id);
        mockMvc.perform(
                put(requestUrl)
                        .content(jsonValue)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void whenDelete() throws Exception {
        int id = 1;
        String requestUrl = String.format("%s/%d", BASE_URL, id);
        mockMvc.perform(delete(requestUrl))
                .andExpect(status().isOk());
    }
}