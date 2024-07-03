package com.my.code.notes_app.controller;

import com.my.code.notes_app.dto.NoteDto;
import com.my.code.notes_app.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/notes")
@Slf4j
public class NoteController {

    private final NoteService noteService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<NoteDto> createNote(@RequestBody @Valid NoteDto noteDto) {
        NoteDto response = noteService.createNote(noteDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<NoteDto> updateNote(@RequestBody @Valid NoteDto noteDto) {
        NoteDto response = noteService.updateNote(noteDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> deleteNote(@PathVariable("id") String id) {
        noteService.deleteNoteById(id);
        return ResponseEntity.ok().build();
    }
}
