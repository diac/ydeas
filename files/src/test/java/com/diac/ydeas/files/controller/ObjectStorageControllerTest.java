package com.diac.ydeas.files.controller;

import com.diac.ydeas.domain.dto.FileDownloadResultDto;
import com.diac.ydeas.domain.dto.StoredObjectDetailsDto;
import com.diac.ydeas.files.service.ObjectStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ObjectStorageController.class)
@AutoConfigureMockMvc
public class ObjectStorageControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private ObjectStorageService objectStorageService;

    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void whenUpload() throws Exception {
        String value = "test";
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", value, value, value.getBytes());
        StoredObjectDetailsDto storedObjectDetailsDto = new StoredObjectDetailsDto(
                value,
                value,
                value,
                value.length()
        );
        Mockito.when(objectStorageService.upload(mockMultipartFile)).thenReturn(storedObjectDetailsDto);
        String expectedResponseBody = objectWriter.writeValueAsString(storedObjectDetailsDto);
        mockMvc.perform(
                        multipart("")
                                .file(mockMultipartFile)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
        Mockito.verify(objectStorageService).upload(mockMultipartFile);
    }

    @Test
    public void whenDownload() throws Exception {
        String value = "test";
        MediaType mediaType = MediaType.TEXT_PLAIN;
        Resource resource = new ByteArrayResource(value.getBytes());
        FileDownloadResultDto fileDownloadResultDto = new FileDownloadResultDto(resource, mediaType.toString());
        Mockito.when(objectStorageService.download(value)).thenReturn(fileDownloadResultDto);
        String requestUrl = String.format("/%s", value);
        mockMvc.perform(
                        get(requestUrl)
                ).andExpect(status().isOk())
                .andExpect(content().bytes(value.getBytes()));
        Mockito.verify(objectStorageService).download(value);
    }

    @Test
    public void whenDelete() throws Exception {
        String value = "test";
        String requestUrl = String.format("/%s", value);
        mockMvc.perform(
                delete(requestUrl)
        ).andExpect(status().isOk());
        Mockito.verify(objectStorageService).delete(value);
    }
}