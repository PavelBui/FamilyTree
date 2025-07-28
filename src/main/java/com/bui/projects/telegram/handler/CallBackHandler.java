package com.bui.projects.telegram.handler;

import com.bui.projects.telegram.BotKeeper;
import com.bui.projects.telegram.config.State;
import com.bui.projects.telegram.service.BotMenuService;
import com.bui.projects.telegram.session.SessionUser;
import com.bui.projects.telegram.session.TelegramRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

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
        try {
            botMenuService.prepare(update);

            Long chatId = getChatId(update);
            SessionUser sessionUser = TelegramRequestContext.requestUser(chatId);

            checkSpecialMessage(update);
            String data = update.getCallbackQuery().getData();
            Integer messageId = getUpdateMessageId(update);
            Integer defaultPersonId = botKeeper.getTelegramBot().getDefaultPersonId();
            if ("go_home".equals(data)) {
                sessionUser.setState(State.HOME);
                log.info("ChatId: {}. Press go_home", chatId);
                botKeeper.getBot().editPhoto(botMenuService.sendHomePointMessage(messageId, sessionUser, defaultPersonId));
            } else if ("go_default".equals(data)) {
                sessionUser.setState(State.DEFAULT);
                log.info("ChatId: {}. Press go_default", chatId);
                botKeeper.getBot().editPhoto(botMenuService.sendDefaultPointMessage(sessionUser, messageId, defaultPersonId));
            } else if (data.startsWith("go_parents") || data.startsWith("go_kids") || data.startsWith("go_siblings") || data.startsWith("go_spouses")) {
                sessionUser.setState(State.TRAVEL);
                log.info("ChatId: {}. Press {}", chatId, data);
                botKeeper.getBot().editPhoto(botMenuService.sendPersonsListMessage(messageId, data));
            } else if (data.startsWith("person_")) {
                sessionUser.setState(State.TRAVEL);
                log.info("ChatId: {}. Press {}", chatId, data);
                botKeeper.getBot().editPhoto(botMenuService.sendPersonMessage(sessionUser, messageId, data.replace("person_", ""), defaultPersonId));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
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
}
