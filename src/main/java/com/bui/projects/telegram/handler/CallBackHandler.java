package com.bui.projects.telegram.handler;

import com.bui.projects.telegram.BotKeeper;
import com.bui.projects.telegram.config.State;
import com.bui.projects.telegram.service.BotMenuService;
import com.bui.projects.telegram.session.SessionUser;
import com.bui.projects.telegram.session.TelegramRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

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
            if ("go_home".equals(data)) {
                sessionUser.setState(State.HOME);
                botKeeper.getBot().sendPhoto(botMenuService.sendHomePointMessage(sessionUser));
            } else if ("go_default".equals(data)) {
                sessionUser.setState(State.DEFAULT);
                botKeeper.getBot().sendPhoto(botMenuService.sendDefaultPointMessage(botKeeper.getTelegramBot().getDefaultPersonId()));
            } else if (data.startsWith("go_parents") || data.startsWith("go_kids") || data.startsWith("go_siblings") || data.startsWith("go_spouses")) {
                sessionUser.setState(State.TRAVEL);
                botKeeper.getBot().sendPhoto(botMenuService.sendPersonsListMessage(data));
            } else if (data.startsWith("person_")) {
                sessionUser.setState(State.TRAVEL);
                botKeeper.getBot().sendPhoto(botMenuService.sendPersonMessage(data.replace("person_", "")));
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
}
