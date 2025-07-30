package com.bui.projects.telegram.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum State {
    START("new_account"),
    HOME("person_page"),
    TRAVEL("travel"),
    PHOTO("photo"),
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
