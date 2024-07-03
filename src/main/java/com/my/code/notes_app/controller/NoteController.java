package com.my.code.notes_app.controller;

import com.my.code.notes_app.dto.NoteDto;
import com.my.code.notes_app.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
