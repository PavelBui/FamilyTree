package com.bui.projects.telegram.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum State {
    START("new_account"),
    SELECT_PROJECT("get_project"),
    DEFAULT("default");
    private final String code;

    public static State findByCode(String code) {
        for (State value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return DEFAULT;
  }
}
