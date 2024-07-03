package com.my.code.notes_app.service.impl;

import com.my.code.notes_app.dto.NoteDto;
import com.my.code.notes_app.entity.NoteEntity;
import com.my.code.notes_app.exception.CustomGlobalException;
import com.my.code.notes_app.mapper.NoteMapper;
import com.my.code.notes_app.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoteServiceImplTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteServiceImpl noteService;

    @Captor
    private ArgumentCaptor<NoteEntity> noteEntityCaptor;

    private NoteDto noteDto;

    @BeforeEach
    public void setUp() {
        noteDto = NoteDto.builder()
                .title("Sample Note")
                .createdDate(LocalDateTime.now())
                .text("This is a sample note.")
                .tags(Arrays.asList("BUSINESS", "PERSONAL"))
                .build();
    }

    @Test
    public void when_title_and_created_date_and_text_and_tags_given_then_return_note() {
        NoteEntity noteEntity = NoteMapper.toEntity(noteDto);
        when(noteRepository.insert(any(NoteEntity.class))).thenReturn(noteEntity);

        NoteDto createdNote = noteService.createNote(noteDto);

        verify(noteRepository).insert(noteEntityCaptor.capture());
        NoteEntity capturedNoteEntity = noteEntityCaptor.getValue();

        assertEquals(noteDto.getTitle(), capturedNoteEntity.getTitle());
        assertEquals(noteDto.getCreatedDate(), capturedNoteEntity.getCreatedDate());
        assertEquals(noteDto.getText(), capturedNoteEntity.getText());
        assertEquals(noteDto.getTags().size(), capturedNoteEntity.getTags().size());
        assertEquals(noteDto.getTags().get(0), capturedNoteEntity.getTags().get(0).name());
        assertEquals(noteDto.getTags().get(1), capturedNoteEntity.getTags().get(1).name());

        assertEquals(noteDto.getTitle(), createdNote.getTitle());
        assertEquals(noteDto.getCreatedDate(), createdNote.getCreatedDate());
        assertEquals(noteDto.getText(), createdNote.getText());
        assertEquals(noteDto.getTags(), createdNote.getTags());
    }

    @Test
    public void when_repository_throws_exception_then_throw_same_exception() {
        NoteEntity noteEntity = NoteMapper.toEntity(noteDto);
        when(noteRepository.insert(any(NoteEntity.class))).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> noteService.createNote(noteDto));
        assertEquals("Database error", exception.getMessage());
        verify(noteRepository).insert(noteEntityCaptor.capture());
    }
}
