package com.my.code.notes_app.service;

import com.my.code.notes_app.dto.NoteDto;
import com.my.code.notes_app.marker_interfaces.CreateMarker;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface NoteService {

    @Validated(CreateMarker.class)
    NoteDto createNote(@Valid NoteDto noteDto);
}
