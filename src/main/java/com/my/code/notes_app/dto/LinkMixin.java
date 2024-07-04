package com.my.code.notes_app.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.Link;

public abstract class LinkMixin {
    @JsonIgnore
    abstract Link getTemplate();

    @JsonIgnore
    abstract boolean isTemplated();
}
