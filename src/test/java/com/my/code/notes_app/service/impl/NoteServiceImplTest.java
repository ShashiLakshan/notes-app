package com.my.code.notes_app.service.impl;

import com.my.code.notes_app.dto.NoteDto;
import com.my.code.notes_app.entity.NoteEntity;
import com.my.code.notes_app.enums.TagType;
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
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.my.code.notes_app.enums.ErrorEnum.NO_RECORD_FOUND;
import static org.junit.jupiter.api.Assertions.*;
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
    private NoteEntity noteEntity;

    @BeforeEach
    public void setUp() {
        noteDto = NoteDto.builder()
                .title("Sample Note")
                .createdDate(LocalDateTime.now())
                .text("This is a sample note.")
                .tags(Arrays.asList("BUSINESS", "PERSONAL"))
                .build();
        noteEntity = NoteMapper.toEntity(noteDto);
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

    @Test
    public void when_update_note_then_return_updated_note() {
        when(noteRepository.findById(noteDto.getId())).thenReturn(Optional.of(noteEntity));
        when(noteRepository.save(any(NoteEntity.class))).thenReturn(noteEntity);

        NoteDto updatedNote = noteService.updateNote(noteDto);

        verify(noteRepository).save(noteEntityCaptor.capture());
        NoteEntity capturedNoteEntity = noteEntityCaptor.getValue();

        assertEquals(noteDto.getTitle(), capturedNoteEntity.getTitle());
        assertEquals(noteDto.getCreatedDate(), capturedNoteEntity.getCreatedDate());
        assertEquals(noteDto.getText(), capturedNoteEntity.getText());
        assertEquals(noteDto.getTags().size(), capturedNoteEntity.getTags().size());
        assertEquals(noteDto.getTags().get(0), capturedNoteEntity.getTags().get(0).name());
        assertEquals(noteDto.getTags().get(1), capturedNoteEntity.getTags().get(1).name());

        assertEquals(noteDto.getTitle(), updatedNote.getTitle());
        assertEquals(noteDto.getCreatedDate(), updatedNote.getCreatedDate());
        assertEquals(noteDto.getText(), updatedNote.getText());
        assertEquals(noteDto.getTags(), updatedNote.getTags());
    }

    @Test
    public void when_note_not_found_then_throw_custom_exception() {
        when(noteRepository.findById(noteDto.getId())).thenReturn(Optional.empty());

        CustomGlobalException exception = assertThrows(CustomGlobalException.class, () -> noteService.updateNote(noteDto));
        assertEquals(NO_RECORD_FOUND.getCode(), exception.getCode());
        assertEquals(String.format(NO_RECORD_FOUND.getMessage(), noteDto.getId()), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void when_delete_note_by_id_then_note_is_deleted() {
        when(noteRepository.findById(noteDto.getId())).thenReturn(Optional.of(noteEntity));
        doNothing().when(noteRepository).delete(noteEntity);

        noteService.deleteNoteById(noteDto.getId());
        verify(noteRepository).delete(noteEntity);
    }

    @Test
    public void when_get_note_by_id_then_return_note() {
        when(noteRepository.findById(noteDto.getId())).thenReturn(Optional.of(noteEntity));

        NoteDto foundNote = noteService.getById(noteDto.getId());

        assertEquals(noteDto.getTitle(), foundNote.getTitle());
        assertEquals(noteDto.getCreatedDate(), foundNote.getCreatedDate());
        assertEquals(noteDto.getText(), foundNote.getText());
        assertEquals(noteDto.getTags(), foundNote.getTags());
    }

    @Test
    public void when_filter_notes_then_return_paginated_notes() {
        List<NoteEntity> notes = Arrays.asList(noteEntity);
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<NoteEntity> page = new PageImpl<>(notes, pageable, notes.size());

        when(noteRepository.findByTagsIn(anyList(), any(Pageable.class))).thenReturn(page);

        Page<NoteDto> result = noteService.filterNotes(Arrays.asList(TagType.BUSINESS, TagType.PERSONAL), 0, 2, Sort.Direction.DESC);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(noteDto.getTitle(), result.getContent().get(0).getTitle());
    }

    @Test
    public void when_get_stats_then_return_note_with_stats() {
        when(noteRepository.findAll()).thenReturn(Arrays.asList(noteEntity));

        List<NoteDto> notesWithStats = noteService.getStats();

        assertEquals(1, notesWithStats.size());
        NoteDto noteWithStats = notesWithStats.get(0);
        assertEquals(noteDto.getTitle(), noteWithStats.getTitle());
        assertTrue(noteWithStats.getStats().containsKey("sample"));
    }

}
