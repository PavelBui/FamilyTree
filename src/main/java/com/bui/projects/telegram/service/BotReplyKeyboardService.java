package com.bui.projects.telegram.service;

import com.bui.projects.telegram.BotKeeper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BotReplyKeyboardService {

    private static final List<String> REPLY_KEYBOARD_BUTTONS = List.of("Родители", "Дети", "Братья и сестры", "Мужья и жены", "К себе", "Это я!");

    private final BotKeeper botKeeper;

    public void sendMenuKeyboard(Update update, String text, boolean isStart) {
        ReplyKeyboard replyKeyboard = generateReplyKeyboard(isStart);
        sendKeyboard(update, text, replyKeyboard);
    }

    private void sendKeyboard(Update update, String text, ReplyKeyboard replyKeyboard) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(getChatId(update));
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(replyKeyboard);
        botKeeper.getTelegramBot().executeMessage(sendMessage);
    }

    private Long getChatId(Update update) {
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId();
        } else {
            return update.getMessage().getChatId();
        }
    }

    public ReplyKeyboard generateReplyKeyboard(boolean isStart) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        if (REPLY_KEYBOARD_BUTTONS.isEmpty() || isStart) {
            ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
            replyKeyboardRemove.setRemoveKeyboard(true);
            return replyKeyboardRemove;
        }
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        int rows = (REPLY_KEYBOARD_BUTTONS.size() + 1) / 2;
        for (int i = 0; i < rows; i++) {
            List<KeyboardButton> keyboardButtonList = new ArrayList<>();
            int leftButtonIndex = i * 2;
            keyboardButtonList.add(prepareButton(REPLY_KEYBOARD_BUTTONS.get(leftButtonIndex)));
            if (leftButtonIndex + 1 < REPLY_KEYBOARD_BUTTONS.size()) {
                keyboardButtonList.add(prepareButton(REPLY_KEYBOARD_BUTTONS.get(leftButtonIndex + 1)));
            }
            keyboardRowList.add(new KeyboardRow(keyboardButtonList));
        }
        keyboardMarkup.setKeyboard(keyboardRowList);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
        keyboardMarkup.setSelective(true);
        return keyboardMarkup;
    }

    private KeyboardButton prepareButton(String text) {
        if (text.startsWith("http:\\")) {
            return KeyboardButton.builder()
                    .text(text)
                    .webApp(new WebAppInfo(text))
                    .build();
        } else {
            return new KeyboardButton(text);
        }
    }
}
