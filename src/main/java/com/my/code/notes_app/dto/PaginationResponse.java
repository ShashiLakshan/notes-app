package com.my.code.notes_app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginationResponse<T> {
    private List<T> notes;
    private int pageNumber;
    private int pageSize;
    private long totalElements;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaginationResponse<?> that = (PaginationResponse<?>) o;
        return pageNumber == that.pageNumber && pageSize == that.pageSize
                && totalElements == that.totalElements && Objects.equals(notes, that.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notes, pageNumber, pageSize, totalElements);
    }
}
