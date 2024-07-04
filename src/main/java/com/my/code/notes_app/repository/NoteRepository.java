package com.my.code.notes_app.repository;

import com.my.code.notes_app.entity.NoteEntity;
import com.my.code.notes_app.enums.TagType;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends MongoRepository<NoteEntity, String> {

    List<NoteEntity> findByTagsIn(List<TagType> tags);
}
