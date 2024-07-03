package com.my.code.notes_app.service;

import com.my.code.notes_app.dto.NoteDto;

public interface NoteService {
    NoteDto createNote(NoteDto noteDto);
}
