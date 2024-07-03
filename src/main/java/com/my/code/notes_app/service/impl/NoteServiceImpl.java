package com.my.code.notes_app.service.impl;

import com.my.code.notes_app.dto.NoteDto;
import com.my.code.notes_app.entity.NoteEntity;
import com.my.code.notes_app.exception.CustomGlobalException;
import com.my.code.notes_app.mapper.NoteMapper;
import com.my.code.notes_app.repository.NoteRepository;
import com.my.code.notes_app.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    @Override
    public NoteDto createNote(NoteDto noteDto) {

        NoteEntity noteEntity = noteRepository.insert(NoteMapper.toEntity(noteDto));
        return NoteMapper.toDto(noteEntity);
    }

    @Override
    public NoteDto updateNote(NoteDto noteDto) {
        NoteEntity noteEntity = getNoteById(noteDto.getId());
        noteEntity = noteRepository.save(NoteMapper.toUpdateEntity(noteDto, noteEntity));
        return NoteMapper.toDto(noteEntity);
    }

    @Override
    public void deleteNoteById(String id) {
        NoteEntity noteEntity = getNoteById(id);
        noteRepository.delete(noteEntity);
    }

    private NoteEntity getNoteById(String id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new CustomGlobalException("AR-100",
                        String.format("record exist : %s", id), HttpStatus.BAD_REQUEST));
    }
}
