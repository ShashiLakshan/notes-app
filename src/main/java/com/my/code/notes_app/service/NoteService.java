package com.my.code.notes_app.service;

import com.my.code.notes_app.dto.NoteDto;
import com.my.code.notes_app.enums.TagType;
import com.my.code.notes_app.marker_interfaces.CreateMarker;
import com.my.code.notes_app.marker_interfaces.UpdateMarker;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;

@Validated
public interface NoteService {

    @Validated(CreateMarker.class)
    NoteDto createNote(@Valid NoteDto noteDto);

    @Validated(UpdateMarker.class)
    NoteDto updateNote(@Valid NoteDto noteDto);

    void deleteNoteById(String id);

    Page<NoteDto> filterNotes(List<TagType> tags, int page, int sizePerPage, Sort.Direction sortDirection);

    NoteDto getById(String id);

    List<NoteDto> getStats();
}
