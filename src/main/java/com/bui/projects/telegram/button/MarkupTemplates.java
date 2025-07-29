package com.bui.projects.telegram.button;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.bui.projects.telegram.util.Constants.DEFAULT_BUTTON;
import static com.bui.projects.telegram.util.Constants.HOME_BUTTON;

@UtilityClass
public class MarkupTemplates {

    public static ReplyKeyboardMarkup requestContactMarkup(String sharePhoneText, String skipText) {
        KeyboardButton sharePhoneKeyboardButton = KeyboardButton.builder()
                .text(sharePhoneText)
                .requestContact(true)
                .build();
        KeyboardButton skipKeyboardButton = KeyboardButton.builder()
                .text(skipText)
                .requestContact(false)
                .build();
        KeyboardRow row = new KeyboardRow(List.of(sharePhoneKeyboardButton, skipKeyboardButton));
        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row))
                .resizeKeyboard(true)
                .selective(true)
                .build();
    }

    public static InlineKeyboardMarkup removeButton(InlineKeyboardMarkup board, String text, boolean isInNewLine) {
        InlineKeyboardButton button = new InlineKeyboardButton(text == null ? " ❌ " : text);
        button.setCallbackData("cancel##");

        List<List<InlineKeyboardButton>> keyboard = board.getKeyboard();
        if (isInNewLine) {
            keyboard.add(getRow(button));
        } else {
            keyboard.get(keyboard.size() - 1).add(button);
        }
        return board;
    }

    public static InlineKeyboardMarkup listButtons(int numberOfColumns, List<String> buttonList) {
        InlineKeyboardMarkup board = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> listButton = new ArrayList<>();
        board.setKeyboard(listButton);
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton;

        int index = 0;
        for (int i = 1; i <= buttonList.size(); i++) {
            inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(buttonList.get(index));
            inlineKeyboardButton.setCallbackData(buttonList.get(index));
            row.add(inlineKeyboardButton);
            index++;

            if (i % numberOfColumns == 0) {
                listButton.add(row);
                row = new ArrayList<>();
            }
            if (index == buttonList.size()) break;
        }

        if (!row.isEmpty()) listButton.add(row);

        return board;
    }

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

            if (e1.getKey().equals(HOME_BUTTON)) return 1;
            if (e2.getKey().equals(HOME_BUTTON)) return -1;

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

    public static InlineKeyboardMarkup addButton(InlineKeyboardMarkup board, String id, String text) {
        InlineKeyboardButton button = new InlineKeyboardButton(text);
        button.setCallbackData(id);

        List<List<InlineKeyboardButton>> keyboard = board.getKeyboard();
        List<InlineKeyboardButton> row = getRow(button);
        keyboard.add(row);

        board.setKeyboard(keyboard);

        return board;
    }

    private static List<InlineKeyboardButton> getRow(InlineKeyboardButton... buttons) {
        return Stream.of(buttons).toList();
    }
}
