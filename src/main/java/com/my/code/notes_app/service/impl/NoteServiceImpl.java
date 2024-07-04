package com.my.code.notes_app.service.impl;

import com.my.code.notes_app.dto.NoteDto;
import com.my.code.notes_app.entity.NoteEntity;
import com.my.code.notes_app.enums.TagType;
import com.my.code.notes_app.exception.CustomGlobalException;
import com.my.code.notes_app.mapper.NoteMapper;
import com.my.code.notes_app.repository.NoteRepository;
import com.my.code.notes_app.service.NoteService;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import static com.my.code.notes_app.enums.ErrorEnum.NO_RECORD_FOUND;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    @Transactional
    @Override
    public NoteDto createNote(NoteDto noteDto) {
        NoteEntity noteEntity = noteRepository.insert(NoteMapper.toEntity(noteDto));
        return NoteMapper.toDto(noteEntity);
    }

    @Transactional
    @Override
    public NoteDto updateNote(NoteDto noteDto) {
        NoteEntity noteEntity = getNoteById(noteDto.getId());
        noteEntity = noteRepository.save(NoteMapper.toUpdateEntity(noteDto, noteEntity));
        return NoteMapper.toDto(noteEntity);
    }

    @Transactional
    @Override
    public void deleteNoteById(String id) {
        NoteEntity noteEntity = getNoteById(id);
        noteRepository.delete(noteEntity);
    }

    @Override
    public Page<NoteDto> filterNotes(List<TagType> tags, int page, int sizePerPage, Sort.Direction sortDirection) {
        Pageable pageable = PageRequest.of(page, sizePerPage, Sort.by(sortDirection, "createdDate"));
        if (ObjectUtils.isEmpty(tags)) {
            return noteRepository.findAll(pageable).map(NoteMapper::toSummaryDto);
        }
        return noteRepository.findByTagsIn(tags, pageable).map(NoteMapper::toSummaryDto);
    }

    @Override
    public NoteDto getById(String id) {
        NoteEntity noteEntity = getNoteById(id);
        return NoteMapper.toDto(noteEntity);
    }

    public List<NoteDto> getStats() {
        List<NoteEntity> noteEntities = noteRepository.findAll();
        return noteEntities.parallelStream()
                .map(NoteMapper::toDto)
                .map(noteDto -> noteDto.setStats(getStats(noteDto.getText())))
                .collect(Collectors.toList());
    }


    private NoteEntity getNoteById(String id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new CustomGlobalException(NO_RECORD_FOUND.getCode(),
                        String.format(NO_RECORD_FOUND.getMessage(), id), HttpStatus.BAD_REQUEST));
    }

    public Map<String, Integer> getStats(String text) {
        if (StringUtils.hasText(text)) {
            String[] words = text.toLowerCase().split("\\W+");
            ConcurrentMap<String, Integer> wordCount = Arrays.stream(words)
                    .parallel()
                    .filter(word -> !word.isEmpty())
                    .collect(Collectors.toConcurrentMap(
                            word -> word,
                            word -> 1,
                            Integer::sum
                    ));

            return wordCount.entrySet()
                    .stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));
        }
        return Collections.emptyMap();
    }
}
