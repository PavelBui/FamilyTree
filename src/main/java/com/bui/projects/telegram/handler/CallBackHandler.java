package com.bui.projects.telegram.handler;

import com.bui.projects.telegram.BotKeeper;
import com.bui.projects.telegram.util.enums.State;
import com.bui.projects.telegram.service.BotMenuService;
import com.bui.projects.telegram.session.SessionUser;
import com.bui.projects.telegram.session.TelegramRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

import static com.bui.projects.telegram.util.Constants.*;

@Component
@Slf4j
public class CallBackHandler extends BaseMethods implements IBaseHandler {

    private final BotMenuService botMenuService;

    public CallBackHandler(BotKeeper botKeeper, BotMenuService botMenuService) {
        super(botKeeper);
        this.botMenuService = botMenuService;
    }

    @Override
    public void handle(Update update) {
        botMenuService.prepare(update);

        Long chatId = getChatId(update);
        SessionUser sessionUser = TelegramRequestContext.requestUser(chatId);

        checkSpecialMessage(update);
        String data = update.getCallbackQuery().getData();
        Integer messageId = getUpdateMessageId(update);
        Integer defaultPersonId = botKeeper.getTelegramBot().getDefaultPersonId();
        provideLog(chatId, data);
        if (HOME_BUTTON.equals(data)) {
            sessionUser.setState(State.HOME);
            botKeeper.getBot().editPhoto(botMenuService.sendHomePointMessage(messageId, sessionUser, defaultPersonId));
            return;
        }
        if (DEFAULT_BUTTON.equals(data)) {
            sessionUser.setState(State.DEFAULT);
            botKeeper.getBot().editPhoto(botMenuService.sendDefaultPointMessage(sessionUser, messageId, defaultPersonId));
            return;
        }
        if (data.startsWith(PERSON_BUTTON_PREFIX)) {
            sessionUser.setState(State.TRAVEL);
            botKeeper.getBot().editPhoto(botMenuService.sendPersonMessage(sessionUser, messageId, data.replace(PERSON_BUTTON_PREFIX, ""), defaultPersonId));
            return;
        }
        sessionUser.setState(State.TRAVEL);
        if (data.endsWith(MULTI_PERSON_BUTTON_SUFFIX)) {
            botKeeper.getBot().editPhoto(botMenuService.sendPersonsListMessage(messageId, data));
        } else {
            String[] components = data.split("_");
            botKeeper.getBot().editPhoto(botMenuService.sendPersonMessage(sessionUser, messageId, components[2], defaultPersonId));
        }
    }

    private Long getChatId(Update update) {
        return update.getCallbackQuery().getMessage().getChatId();
    }

    private void checkSpecialMessage(Update update) {
        if (update.getCallbackQuery().getData().startsWith("cancel")) {
            deleteMessage(update.getCallbackQuery().getMessage().getChatId(), update.getCallbackQuery().getMessage().getMessageId());
        }
    }

    private Integer getUpdateMessageId(Update update) {
        return Optional.ofNullable(update.getCallbackQuery())
                .map(CallbackQuery::getMessage)
                .map(MaybeInaccessibleMessage::getMessageId).orElse(null);
    }

    private void provideLog(Long chatId, String data) {
        log.info("ChatId: {}. Press {}", chatId, data);
    }
}
