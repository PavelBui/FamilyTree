package com.bui.projects.telegram.button;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.bui.projects.telegram.util.Constants.*;

@UtilityClass
public class MarkupTemplates {

    public static InlineKeyboardMarkup listEntityButtons(Map<String, String> map, int numberOfColumns) {
        InlineKeyboardMarkup board = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> listButton = new ArrayList<>();
        board.setKeyboard(listButton);
        List<InlineKeyboardButton> row = new ArrayList<>();

        int index = 1;
        List<Map.Entry<String, String>> sortedEntries = new ArrayList<>(map.entrySet());
        sortedEntries.sort((e1, e2) -> {
            if (e1.getKey().equals(DEFAULT_BUTTON)) return 1;
            if (e2.getKey().equals(DEFAULT_BUTTON)) return -1;

            if (e1.getKey().equals(HOME_BUTTON)) {
                if (e2.getKey().equals(DEFAULT_BUTTON)) return -1;
                return 1;
            }
            if (e2.getKey().equals(HOME_BUTTON)) {
                if (e1.getKey().equals(DEFAULT_BUTTON)) return 1;
                return -1;
            }

            if (e1.getKey().startsWith(PHOTO_BUTTON_PREFIX)) {
                if (e2.getKey().equals(HOME_BUTTON) || e2.getKey().equals(DEFAULT_BUTTON)) return -1;
                return 1;
            }
            if (e2.getKey().startsWith(PHOTO_BUTTON_PREFIX)) {
                if (e1.getKey().equals(HOME_BUTTON) || e1.getKey().equals(DEFAULT_BUTTON)) return 1;
                return -1;
            }

            return e1.getValue().compareTo(e2.getValue());
        });

        for (Map.Entry<String, String> entry : sortedEntries) {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(entry.getValue() != null ? entry.getValue() : "");
            if (entry.getKey().endsWith("url")) {
                String url = entry.getKey().split("##")[0];
                inlineKeyboardButton.setUrl(url);
            } else {
                inlineKeyboardButton.setCallbackData(entry.getKey());
            }
            row.add(inlineKeyboardButton);

            if (index++ % numberOfColumns == 0) {
                listButton.add(row);
                row = new ArrayList<>();
            }
        }

        if (!row.isEmpty()) listButton.add(row);

        return board;
    }
}