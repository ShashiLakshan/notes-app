package com.my.code.notes_app.mapper;

import com.my.code.notes_app.dto.NoteDto;
import com.my.code.notes_app.entity.NoteEntity;
import com.my.code.notes_app.enums.TagType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class NoteMapper {

    private NoteMapper() {}

    public static NoteEntity toEntity(NoteDto noteDto) {
        return NoteEntity
                .builder()
                .title(noteDto.getTitle())
                .createdDate(noteDto.getCreatedDate())
                .text(noteDto.getText())
                .tags(!CollectionUtils.isEmpty(noteDto.getTags()) ? toTagTypeList(noteDto.getTags()) : null)
                .build();
    }

    public static NoteEntity toUpdateEntity(NoteDto noteDto, NoteEntity noteEntity) {

        if (StringUtils.hasText(noteDto.getTitle()) && !noteDto.getTitle().equals(noteEntity.getTitle())) {
            noteEntity.setTitle(noteDto.getTitle());
        }
        if (StringUtils.hasText(noteDto.getText()) && !noteDto.getText().equals(noteEntity.getText())) {
            noteEntity.setText(noteDto.getText());
        }
        if (!ObjectUtils.isEmpty(noteDto.getCreatedDate()) && !noteDto.getCreatedDate().equals(noteEntity.getCreatedDate())) {
            noteEntity.setCreatedDate(noteDto.getCreatedDate());
        }
        if (!CollectionUtils.isEmpty(noteDto.getTags()) && !toTagTypeList(noteDto.getTags()).equals(noteEntity.getTags())) {
            noteEntity.setTags(toTagTypeList(noteDto.getTags()));
        }
        return noteEntity;
    }

    public static NoteDto toDto(NoteEntity noteEntity) {
        return NoteDto
                .builder()
                .id(noteEntity.getId())
                .title(noteEntity.getTitle())
                .createdDate(noteEntity.getCreatedDate())
                .text(noteEntity.getText())
                .tags(noteEntity.getTags() != null ? toTagStringList(noteEntity.getTags()) : null)
                .build();
    }

    public static NoteDto toSummaryDto(NoteEntity noteEntity) {
        return NoteDto
                .builder()
                .id(noteEntity.getId())
                .title(noteEntity.getTitle())
                .createdDate(noteEntity.getCreatedDate())
                .build();
    }

    private static List<TagType> toTagTypeList(List<String> tags) {
        return tags.stream()
                .map(TagType::valueOf)
                .collect(Collectors.toList());
    }

    private static List<String> toTagStringList(List<TagType> tags) {
        return tags.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
