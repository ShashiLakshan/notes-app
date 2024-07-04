package com.my.code.notes_app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.my.code.notes_app.annotation.ValidTag;
import com.my.code.notes_app.marker_interfaces.CreateMarker;
import com.my.code.notes_app.marker_interfaces.UpdateMarker;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName(value = "Note")
public class NoteDto extends RepresentationModel<NoteDto> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(groups = UpdateMarker.class)
    private String id;

    @NotBlank(groups = CreateMarker.class)
    private String title;

    @NotNull(groups = CreateMarker.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @NotBlank(groups = CreateMarker.class)
    private String text;

    @ValidTag
    private List<String> tags;

    private Map<String, Integer> stats;

    public NoteDto setStats(Map<String, Integer> stats) {
        this.stats = stats;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteDto noteDto = (NoteDto) o;
        return Objects.equals(id, noteDto.id) && Objects.equals(title, noteDto.title)
                && Objects.equals(createdDate, noteDto.createdDate) && Objects.equals(text, noteDto.text)
                && Objects.equals(tags, noteDto.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, createdDate, text, tags);
    }

    @Override
    public String toString() {
        return "NoteDto{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", createdDate=" + createdDate +
                ", text='" + text + '\'' +
                ", tags=" + tags +
                '}';
    }
}
