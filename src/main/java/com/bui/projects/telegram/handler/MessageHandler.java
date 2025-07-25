package com.bui.projects.telegram.handler;

import com.bui.projects.telegram.BotKeeper;
import com.bui.projects.telegram.service.BotMenuService;
import com.bui.projects.telegram.session.SessionUser;
import com.bui.projects.telegram.session.TelegramRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;


@Slf4j
@Component
public class MessageHandler extends BaseMethods implements IBaseHandler {

//    private final BotReplyKeyboardService botReplyKeyboardService;
    private final BotMenuService botMenuService;

    public MessageHandler(
            BotKeeper botKeeper,
//            BotReplyKeyboardService botReplyKeyboardService,
            BotMenuService botMenuService) {
        super(botKeeper);
//        this.botReplyKeyboardService = botReplyKeyboardService;
        this.botMenuService = botMenuService;
    }

    @Override
    public void handle(Update update) {
        try {
            botMenuService.prepare(update);

            start(update);
//            Long chatId = getChatId(update);
//            SessionUser sessionUser = TelegramRequestContext.requestUser(chatId);
            menu(update);
//            switch (sessionUser.getState()) {
//                case START -> menu(update);
//                case HOME -> menu(update);
//                case TRAVEL -> menu(update);
//                case DEFAULT -> menu(update);
//            }
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        }
    }

    private void menu(Update update) throws IOException {
        Long chatId = getChatId(update);
        SessionUser sessionUser = TelegramRequestContext.requestUser(chatId);
        String messageText = update.getMessage().getText();
        switch (messageText) {
//            case "/me" -> changeUserLanguage(sessionUser);
            case "/home" -> botKeeper.getBot().sendPhoto(botMenuService.sendHomePointMessage(sessionUser));
        }
    }

    private void start(Update update) {
        String text = update.getMessage().getText();
        if (text == null || !text.startsWith("/start")) {
            return;
        }
        Long chatId = getChatId(update);
        SessionUser sessionUser = TelegramRequestContext.requestUser(chatId);
// Пока не используем ReplyKeyboard
//        botReplyKeyboardService.sendMenuKeyboard(update, "Добро пожаловать в Семейное древо!", true);
        botKeeper.getBot().sendPhoto(botMenuService.sendStartPointMessage(sessionUser));
    }

    private Long getChatId(Update update) {
        return update.getMessage().getChatId();
    }
}
