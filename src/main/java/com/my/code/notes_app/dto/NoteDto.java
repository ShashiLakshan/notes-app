package com.my.code.notes_app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.my.code.notes_app.annotation.ValidTag;
import com.my.code.notes_app.marker_interfaces.CreateMarker;
import com.my.code.notes_app.marker_interfaces.UpdateMarker;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName(value = "Note")
public class NoteDto implements Serializable {

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

}
