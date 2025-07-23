package com.bui.projects.telegram.handler;

import com.bui.projects.telegram.BotKeeper;
import com.bui.projects.telegram.service.BotReplyKeyboardService;
import com.bui.projects.telegram.service.BotSectionService;
import com.bui.projects.telegram.service.BotService;
import com.bui.projects.telegram.session.SessionUser;
import com.bui.projects.telegram.session.TelegramRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static com.bui.projects.telegram.util.Commands.COMMAND_PREFIX;

@Slf4j
@Component
public class MessageHandler extends BaseMethods implements IBaseHandler {

    private final BotService botService;
    private final BotReplyKeyboardService botReplyKeyboardService;
    private final BotSectionService botSectionService;

    public MessageHandler(
            BotKeeper botKeeper,
            BotService botService,
            BotReplyKeyboardService botReplyKeyboardService,
            BotSectionService botSectionService) {
        super(botKeeper);
        this.botService = botService;
        this.botReplyKeyboardService = botReplyKeyboardService;
        this.botSectionService = botSectionService;
    }

    @Override
    public void handle(Update update) {
        try {
            addUserIdToLogs(update);
            botSectionService.prepare(update);
            botService.prepare(update);

            Long chatId = getChatId(update);
            SessionUser sessionUser = TelegramRequestContext.requestUser(chatId);

            start(update);
            if (isCommand(update)) {
                log.info("message: {}", update.getMessage().getText());
            }
            menu(update);
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
        Long chatId = getChatId(update);
        SessionUser sessionUser = TelegramRequestContext.requestUser(chatId);

        String text = update.getMessage().getText();
        if (text == null || !text.startsWith("/start")) return;

        botReplyKeyboardService.sendMenuKeyboard(update, "Вы уже зарегестрированы в боте");
//        botService.reactivateAccount(getChatId(update));
        botKeeper.getBot().sendPhoto(botSectionService.sendSectionPhotoMessage(sessionUser));
    }

    private boolean isCommand(Update update) {
        return Optional.ofNullable(update.getMessage())
                .map(Message::getText)
                .filter(text -> text.startsWith(COMMAND_PREFIX))
                .isPresent();
    }

    private Long getChatId(Update update) {
        return update.getMessage().getChatId();
    }

    private void addUserIdToLogs(Update update) {
        String chatId = Optional.ofNullable(update.getMessage())
                .map(Message::getChatId)
                .map(Object::toString).orElse("Undefined");
        MDC.put("chatId", chatId);
        MDC.put("typeId", "message");
    }
}
