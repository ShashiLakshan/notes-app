package com.my.code.notes_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.code.notes_app.dto.NoteDto;
import com.my.code.notes_app.enums.TagType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ContextConfiguration(initializers = NoteControllerIntegrationTest.Initializer.class)
public class NoteControllerIntegrationTest {

    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.12");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    //
    private NoteDto noteDto;

    @BeforeEach
    public void setUp() {
        noteDto = NoteDto.builder()
                .title("Sample Note")
                .createdDate(LocalDateTime.now())
                .text("This is a sample note.")
                .tags(Arrays.asList(TagType.BUSINESS.name(), TagType.PERSONAL.name()))
                .build();
    }

    @Order(3)
    @Test
    public void when_title_and_created_date_and_text_and_tags_given_note_should_be_created() throws Exception {
        mockMvc.perform(post("/api/v1/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noteDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.Title").value("Sample Note"))
                .andExpect(jsonPath("$.CreatedDate").exists())
                .andExpect(jsonPath("$.Text").value("This is a sample note."))
                .andExpect(jsonPath("$.Tags[0]").value(TagType.BUSINESS.name()))
                .andExpect(jsonPath("$.Tags[1]").value(TagType.PERSONAL.name()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Order(4)
    @Test
    public void when_title_is_empty_then_exception_should_be_occurred() throws Exception {
        noteDto.setTitle("");
        mockMvc.perform(post("/api/v1/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noteDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].Code").value("APP_400"))
                .andExpect(jsonPath("$[0].Message").value("Title must not be blank"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Order(5)
    @Test
    public void when_updating_existing_note_without_id_then_exception_should_be_occurred() throws Exception {
        String responseContent = mockMvc.perform(post("/api/v1/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noteDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        NoteDto createdNote = objectMapper.readValue(responseContent, NoteDto.class);
        createdNote.setTitle("Updated Note Title");

        mockMvc.perform(put("/api/v1/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdNote)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Title").value("Updated Note Title"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Order(6)
    @Test
    public void when_deleting_existing_note_then_it_should_be_deleted() throws Exception {

        String responseContent = mockMvc.perform(post("/api/v1/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noteDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        NoteDto createdNote = objectMapper.readValue(responseContent, NoteDto.class);

        mockMvc.perform(delete("/api/v1/notes/{id}", createdNote.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Order(7)
    @Test
    public void when_getting_existing_note_by_id_then_note_should_be_returned() throws Exception {

        String responseContent = mockMvc.perform(post("/api/v1/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noteDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        NoteDto createdNote = objectMapper.readValue(responseContent, NoteDto.class);

        mockMvc.perform(get("/api/v1/notes/{id}", createdNote.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Id").value(createdNote.getId()))
                .andExpect(jsonPath("$.Title").value("Sample Note"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(2)
    public void when_filtering_notes_then_paginated_response_should_be_returned() throws Exception {

        for (int i = 0; i < 5; i++) {
            noteDto.setTitle("Sample Note " + i);
            mockMvc.perform(post("/api/v1/notes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(noteDto)))
                    .andExpect(status().isCreated());
        }

        mockMvc.perform(get("/api/v1/notes")
                        .param("page", "0")
                        .param("size", "2")
                        .param("sortDirection", "DESC")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.PageSize").value(2))
                .andExpect(jsonPath("$.PageNumber").value(0))
                .andDo(MockMvcResultHandlers.print());
    }

    @Order(1)
    @Test
    public void when_requesting_stats_then_stats_should_be_returned() throws Exception {
        noteDto.setText("this is the the first the");
        mockMvc.perform(post("/api/v1/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noteDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/notes/with-stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].Stats.this").value(1))
                .andExpect(jsonPath("$[0].Stats.is").value(1))
                .andExpect(jsonPath("$[0].Stats.the").value(3))
                .andExpect(jsonPath("$[0].Stats.first").value(1))

                .andDo(MockMvcResultHandlers.print());
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.data.mongodb.uri=" + mongoDBContainer.getReplicaSetUrl()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
