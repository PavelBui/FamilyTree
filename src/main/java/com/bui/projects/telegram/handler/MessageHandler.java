package com.bui.projects.telegram.handler;

import com.bui.projects.telegram.BotKeeper;
import com.bui.projects.telegram.service.BotMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.bui.projects.telegram.util.Constants.*;


@Slf4j
@Component
public class MessageHandler {

    public final BotKeeper botKeeper;
//    private final BotReplyKeyboardService botReplyKeyboardService;
    private final BotMenuService botMenuService;

    public MessageHandler(
            BotKeeper botKeeper,
//            BotReplyKeyboardService botReplyKeyboardService,
            BotMenuService botMenuService) {
        this.botKeeper = botKeeper;
//        this.botReplyKeyboardService = botReplyKeyboardService;
        this.botMenuService = botMenuService;
    }

    public void handle(Update update) {
        botMenuService.prepare(update);

        String messageText = update.getMessage().getText();
        if (messageText == null) {
            return;
        }
        Long chatId = getChatId(update);
        provideLog(chatId, messageText);
        if (messageText.startsWith(START_COMMAND)) {
//            Пока не используем ReplyKeyboard
//            botReplyKeyboardService.sendMenuKeyboard(update, "Добро пожаловать в Семейное древо!", true);
            botKeeper.getBot().sendPhoto(botMenuService.prepareStartPointSendMessage());
            return;
        }
        Integer defaultPersonId = botKeeper.getTelegramBot().getDefaultPersonId();
        switch (messageText) {
            case HOME_COMMAND -> botKeeper.getBot().sendPhoto(botMenuService.prepareHomePointSendPhoto(defaultPersonId));
            case ME_COMMAND -> botKeeper.getBot().sendPhoto(botMenuService.prepareHomePointSendPhoto(defaultPersonId));
        }
    }

    private Long getChatId(Update update) {
        return update.getMessage().getChatId();
    }

    private void provideLog(Long chatId, String messageText) {
        log.info("ChatId: {}. Command {}", chatId, messageText);
    }
}
