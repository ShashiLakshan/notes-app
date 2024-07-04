package com.my.code.notes_app.controller;

import com.my.code.notes_app.dto.NoteDto;
import com.my.code.notes_app.dto.PaginationResponse;
import com.my.code.notes_app.enums.TagType;
import com.my.code.notes_app.service.NoteService;
import jakarta.validation.Valid;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

    @GetMapping(value = "/with-stats", produces = "application/json")
    public ResponseEntity<List<NoteDto>> getStats() {
        return new ResponseEntity<>(noteService.getStats(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<NoteDto> getById(@PathVariable String id) {
        return new ResponseEntity<>(noteService.getById(id), HttpStatus.OK);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<PaginationResponse<NoteDto>> filterNotes(
            @RequestParam(required = false) List<TagType> tags,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection) {

        Page<NoteDto> notePage = noteService.filterNotes(tags, page, size, sortDirection);
        notePage.forEach(note -> {
            note.add(WebMvcLinkBuilder.linkTo(methodOn(NoteController.class).getById(note.getId())).withSelfRel());
        });

        PaginationResponse<NoteDto> response = new PaginationResponse<>(
                notePage.getContent(),
                notePage.getNumber(),
                notePage.getSize(),
                notePage.getTotalElements()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
