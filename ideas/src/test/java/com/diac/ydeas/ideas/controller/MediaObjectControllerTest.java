package com.diac.ydeas.ideas.controller;

import com.diac.ydeas.domain.dto.IdeaMediaObjectAssociationDto;
import com.diac.ydeas.domain.model.MediaObject;
import com.diac.ydeas.ideas.service.MediaObjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MediaObjectController.class)
@AutoConfigureMockMvc
public class MediaObjectControllerTest {

    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private MediaObjectService mediaObjectService;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void whenUploadForIdea() throws Exception {
        String value = "test";
        int intValue = 1;
        UUID uuid = UUID.randomUUID();
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn(uuid.toString());
        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                value,
                MediaType.IMAGE_PNG_VALUE,
                value.getBytes()
        );
        MediaObject mediaObject = MediaObject.builder()
                .id(intValue)
                .url(value)
                .build();
        Mockito.when(mediaObjectService.uploadFileForIdea(multipartFile, intValue, uuid))
                .thenReturn(mediaObject);
        String requestUrl = "/media/upload_for_idea";
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .multipart(requestUrl)
                .file((MockMultipartFile) multipartFile)
                .param("ideaId", String.valueOf(intValue))
                .principal(principal);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated());
        Mockito.verify(mediaObjectService).uploadFileForIdea(multipartFile, intValue, uuid);
    }

    @Test
    public void whenAssociateWithIdea() throws Exception {
        int id = 1;
        UUID uuid = UUID.randomUUID();
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn(uuid.toString());
        String requestUrl = "/media/idea_attachment";
        IdeaMediaObjectAssociationDto ideaMediaObjectAssociationDto = new IdeaMediaObjectAssociationDto(
                id,
                id
        );
        String requestBody = OBJECT_WRITER.writeValueAsString(ideaMediaObjectAssociationDto);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(requestUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .principal(principal);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
        Mockito.verify(mediaObjectService).associateWithIdea(id, id, uuid);
    }

    @Test
    public void whenDissociateWithIdea() throws Exception {
        int intValue = 1;
        String id = String.valueOf(intValue);
        UUID uuid = UUID.randomUUID();
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn(uuid.toString());
        String requestUrl = "/media/idea_attachment";
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(requestUrl)
                .param("mediaObjectId", id)
                .param("ideaId", id)
                .principal(principal);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
        Mockito.verify(mediaObjectService).dissociateWithIdea(intValue, intValue, uuid);
    }
}