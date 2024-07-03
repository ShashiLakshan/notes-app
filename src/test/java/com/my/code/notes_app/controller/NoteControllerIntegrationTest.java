package com.my.code.notes_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.code.notes_app.dto.NoteDto;
import com.my.code.notes_app.enums.TagType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = NoteControllerIntegrationTest.Initializer.class)
public class NoteControllerIntegrationTest {

    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.12");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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


    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.data.mongodb.uri=" + mongoDBContainer.getReplicaSetUrl()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
