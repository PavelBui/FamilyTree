package com.bui.projects.telegram.service;

import com.bui.projects.telegram.BotKeeper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Service
@RequiredArgsConstructor
public class BotReplyKeyboardService {

    private final BotKeeper botKeeper;

    public void sendMenuKeyboard(Update update, String text) {
        Long chatId = getChatId(update);
        ReplyKeyboard replyKeyboard = generateReplyKeyboard();
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

    public ReplyKeyboard generateReplyKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
//        List<CustomerReplyKeyboardEntity> customerReplyKeyboardEntityList = keyboardList();
//        if (customerReplyKeyboardEntityList.isEmpty()) {
//            ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
//            replyKeyboardRemove.setRemoveKeyboard(true);
//            return replyKeyboardRemove;
//        }
//        List<KeyboardRow> keyboardRowList = new ArrayList<>();
//        int rows = (customerReplyKeyboardEntityList.size() + 1) / 2;
//        for (int i = 0; i < rows; i++) {
//            List<KeyboardButton> keyboardButtonList = new ArrayList<>();
//            int leftButtonIndex = i * 2;
//            keyboardButtonList.add(prepareButton(customerReplyKeyboardEntityList.get(leftButtonIndex) ,lang));
//            if (leftButtonIndex + 1 < customerReplyKeyboardEntityList.size()) {
//                keyboardButtonList.add(prepareButton(customerReplyKeyboardEntityList.get(leftButtonIndex + 1) ,lang));
//            }
//            keyboardRowList.add(new KeyboardRow(keyboardButtonList));
//        }
//        keyboardMarkup.setKeyboard(keyboardRowList);
//        keyboardMarkup.setResizeKeyboard(true);
//        keyboardMarkup.setOneTimeKeyboard(false);
//        keyboardMarkup.setSelective(true);
        return keyboardMarkup;
    }

//    KeyboardButton prepareButton(CustomerReplyKeyboardEntity customerReplyKeyboardEntity, Lang lang) {
//        Set<CustomerReplyKeyboardContentEntity> customerReplyKeyboardContentEntityList = customerReplyKeyboardEntity.getReplyKeyboardContent();
//        CustomerReplyKeyboardContentEntity customerReplyKeyboardContentEntity = customerReplyKeyboardContentEntityList.stream()
//                .filter(entity -> lang.toString().equals(entity.getLanguage().getLanguage()))
//                .findFirst().get();
//        String buttonText = customerReplyKeyboardContentEntity.getReplyKeyboardText();
//        String link = customerReplyKeyboardContentEntity.getReplyKeyboardLink();
//        if (isTrue(customerReplyKeyboardEntity.getIsLink())) {
//            return KeyboardButton.builder()
//                    .text(buttonText)
//                    .webApp(new WebAppInfo(link))
//                    .build();
//        } else {
//            return new KeyboardButton(buttonText);
//        }
//    }
}
