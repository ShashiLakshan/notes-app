package com.my.code.notes_app.service.impl;

import com.my.code.notes_app.dto.NoteDto;
import com.my.code.notes_app.entity.NoteEntity;
import com.my.code.notes_app.mapper.NoteMapper;
import com.my.code.notes_app.repository.NoteRepository;
import com.my.code.notes_app.service.NoteService;
import lombok.RequiredArgsConstructor;
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
}
