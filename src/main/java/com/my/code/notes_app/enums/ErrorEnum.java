package com.my.code.notes_app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorEnum {
    NO_RECORD_FOUND("N_APP_100", "No record found with %s.");
    private final String code;
    private final String message;
}
