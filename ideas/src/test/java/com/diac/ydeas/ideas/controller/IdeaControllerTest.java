package com.diac.ydeas.ideas.controller;

import com.diac.ydeas.domain.exception.ResourceNotFoundException;
import com.diac.ydeas.domain.model.Idea;
import com.diac.ydeas.domain.dto.IdeaInputDto;
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
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IdeaController.class)
@AutoConfigureMockMvc
public class IdeaControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private FilterChainProxy filterChainProxy;

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
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
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
        UUID uuid = UUID.randomUUID();
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn(uuid.toString());
        IdeaInputDto ideaInputDto = IdeaInputDto.builder()
                .title(value)
                .description(value)
                .build();
        LocalDateTime now = LocalDateTime.now();
        Idea savedIdea = Idea.builder()
                .id(id)
                .title(value)
                .description(value)
                .authorUuid(uuid)
                .createdAt(now)
                .build();
        String requestBody = OBJECT_WRITER.writeValueAsString(ideaInputDto);
        String responseBody = OBJECT_WRITER.writeValueAsString(savedIdea);
        Mockito.when(ideaService.add(ideaInputDto, uuid)).thenReturn(savedIdea);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(BASE_URL)
                .principal(principal)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().json(responseBody));
    }

    @Test
    public void whenPostWithNullValuesThenResponseStatusIsBadRequest() throws Exception {
        IdeaInputDto ideaInputDto = new IdeaInputDto();
        String requestBody = OBJECT_WRITER.writeValueAsString(ideaInputDto);
        mockMvc.perform(
                post(BASE_URL)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void whenPostWithBlankValuesThenResponseStatusIsBadRequest() throws Exception {
        IdeaInputDto ideaInputDto = IdeaInputDto.builder()
                .title("")
                .description("")
                .build();
        String requestBody = OBJECT_WRITER.writeValueAsString(ideaInputDto);
        mockMvc.perform(
                post(BASE_URL)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void whenPut() throws Exception {
        int id = 1;
        UUID uuid = UUID.randomUUID();
        String value = "test";
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn(uuid.toString());
        LocalDateTime now = LocalDateTime.now();
        IdeaInputDto ideaInputDto = IdeaInputDto.builder()
                .title(value)
                .description(value)
                .build();
        Idea idea = Idea.builder()
                .id(id)
                .title(value)
                .description(value)
                .authorUuid(uuid)
                .createdAt(now)
                .build();
        String jsonValue = OBJECT_WRITER.writeValueAsString(idea);
        String requestUrl = String.format("%s/%d", BASE_URL, id);
        Mockito.when(ideaService.update(id, ideaInputDto, uuid))
                .thenReturn(idea);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(requestUrl)
                .principal(principal)
                .content(jsonValue)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(jsonValue));
    }

    @Test
    public void whenPutWithNullValuesThenResponseStatusIsBadRequest() throws Exception {
        int id = 1;
        IdeaInputDto ideaInputDto = new IdeaInputDto();
        String jsonValue = OBJECT_WRITER.writeValueAsString(ideaInputDto);
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
        IdeaInputDto ideaInputDto = IdeaInputDto.builder()
                .title("")
                .description("")
                .build();
        String jsonValue = OBJECT_WRITER.writeValueAsString(ideaInputDto);
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
        UUID uuid = UUID.randomUUID();
        String requestUrl = String.format("%s/%d", BASE_URL, id);
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn(uuid.toString());
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(requestUrl)
                .principal(principal);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }
}