package com.my.code.notes_app.mapper;

import com.my.code.notes_app.dto.NoteDto;
import com.my.code.notes_app.entity.NoteEntity;

public class NoteMapper {

    private NoteMapper() {}

    public static NoteEntity toEntity(NoteDto noteDto) {
        return NoteEntity
                .builder()
                .title(noteDto.getTitle())
                .createdDate(noteDto.getCreatedDate())
                .text(noteDto.getText())
                .tags(noteDto.getTags())
                .build();
    }

    public static NoteDto toDto(NoteEntity noteEntity) {
        return NoteDto
                .builder()
                .title(noteEntity.getTitle())
                .createdDate(noteEntity.getCreatedDate())
                .text(noteEntity.getText())
                .tags(noteEntity.getTags())
                .build();
    }
}
