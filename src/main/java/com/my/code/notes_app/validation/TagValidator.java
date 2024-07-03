package com.my.code.notes_app.validation;

import com.my.code.notes_app.annotation.ValidTag;
import com.my.code.notes_app.enums.TagType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TagValidator implements ConstraintValidator<ValidTag, List<String>> {

    private static final Set<String> ALLOWED_TAGS = Stream.of(TagType.values())
            .map(Enum::name)
            .collect(Collectors.toSet());

    @Override
    public boolean isValid(List<String> tags, ConstraintValidatorContext context) {
        if (ObjectUtils.isEmpty(tags)) {
            return true;
        }
        return ALLOWED_TAGS.containsAll(tags);
    }
}