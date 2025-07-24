package com.bui.projects.telegram.handler;

import com.bui.projects.telegram.BotKeeper;
import com.bui.projects.telegram.service.BotReplyKeyboardService;
import com.bui.projects.telegram.service.BotMenuService;
import com.bui.projects.telegram.service.BotService;
import com.bui.projects.telegram.session.SessionUser;
import com.bui.projects.telegram.session.TelegramRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Slf4j
@Component
public class MessageHandler extends BaseMethods implements IBaseHandler {

    private final BotService botService;
    private final BotReplyKeyboardService botReplyKeyboardService;
    private final BotMenuService botMenuService;

    public MessageHandler(
            BotKeeper botKeeper,
            BotService botService,
            BotReplyKeyboardService botReplyKeyboardService,
            BotMenuService botMenuService) {
        super(botKeeper);
        this.botService = botService;
        this.botReplyKeyboardService = botReplyKeyboardService;
        this.botMenuService = botMenuService;
    }

    @Override
    public void handle(Update update) {
        try {
            botMenuService.prepare(update);
            botService.prepare(update);

            start(update);
            Long chatId = getChatId(update);
            SessionUser sessionUser = TelegramRequestContext.requestUser(chatId);
            switch (sessionUser.getState()) {
                case START -> menu(update);
                case HOME -> menu(update);
                case TRAVEL -> menu(update);
                case DEFAULT -> menu(update);
            }
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        }
    }

    private void menu(Update update) throws TelegramApiException {
        Long chatId = getChatId(update);
        SessionUser sessionUser = TelegramRequestContext.requestUser(chatId);
        String messageText = update.getMessage().getText();
//        switch (messageText) {
//            case CHANGE_LANGUAGE_COMMAND -> changeUserLanguage(sessionUser);
//            case ASK_COMMAND -> startChatGPT(update);
//            case STOP_ASK_COMMAND -> stopChatGPT(update);
//            case CHANGE_REGION_COMMAND -> changeUserRegion(update);
//        }
    }

    private void start(Update update) {
        String text = update.getMessage().getText();
        if (text == null || !text.startsWith("/start")) {
            return;
        }
        botReplyKeyboardService.sendMenuKeyboard(update, "Добро пожаловать в Семейное древо!", true);
        botKeeper.getBot().sendPhoto(botMenuService.sendStartPointMessage());
    }

    private Long getChatId(Update update) {
        return update.getMessage().getChatId();
    }
}
