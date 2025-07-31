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
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

import static com.bui.projects.telegram.util.Constants.*;

@Component
@Slf4j
public class CallBackHandler {

    public final BotKeeper botKeeper;
    private final BotMenuService botMenuService;

    public CallBackHandler(BotKeeper botKeeper, BotMenuService botMenuService) {
        this.botKeeper = botKeeper;
        this.botMenuService = botMenuService;
    }

    public void handle(Update update) {
        botMenuService.prepare(update);

        Long chatId = getChatId(update);
        SessionUser sessionUser = TelegramRequestContext.requestUser(chatId);

        String data = update.getCallbackQuery().getData();
        Integer messageId = getUpdateMessageId(update);
        Integer defaultPersonId = botKeeper.getTelegramBot().getDefaultPersonId();
        provideLog(chatId, data);
        if (HOME_BUTTON.equals(data)) {
            sessionUser.setState(State.HOME);
            botKeeper.getBot().editPhoto(botMenuService.prepareHomePointEditMessageMedia(messageId, defaultPersonId));
            return;
        }
        if (DEFAULT_BUTTON.equals(data)) {
            sessionUser.setState(State.DEFAULT);
            botKeeper.getBot().editPhoto(botMenuService.prepareDefaultPointEditMessageMedia(messageId, defaultPersonId));
            return;
        }
        if (data.startsWith(PHOTO_BUTTON_PREFIX)) {
            sessionUser.setState(State.PHOTO);
            List<Message> photoMessageList = botKeeper.getBot().sendMediaGroup(botMenuService.preparePhotosSendMediaGroup(data.replace(PHOTO_BUTTON_PREFIX, "")));
            botKeeper.getBot().executeMessage(botMenuService.prepareSendMessage(data.replace(PHOTO_BUTTON_PREFIX, ""), photoMessageList));
            botKeeper.getBot().deleteMessage(botMenuService.prepareDeleteMessage(messageId));
            return;
        }
        if (data.startsWith(PHOTO_BACK_BUTTON_PREFIX)) {
            sessionUser.setState(State.TRAVEL);
            String[] companents = data.split("#");
            if (companents.length > 1) {
                for (int i = 1; i < companents.length; i++) {
                    botKeeper.getBot().deleteMessage(botMenuService.prepareDeleteMessage(Integer.parseInt(companents[i])));
                }
            }
            botKeeper.getBot().editPhoto(botMenuService.preparePersonEditMessageMedia(messageId, companents[0].replace(PHOTO_BACK_BUTTON_PREFIX, ""), defaultPersonId));
            return;
        }
        if (data.startsWith(PERSON_BUTTON_PREFIX)) {
            sessionUser.setState(State.TRAVEL);
            botKeeper.getBot().editPhoto(botMenuService.preparePersonEditMessageMedia(messageId, data.replace(PERSON_BUTTON_PREFIX, ""), defaultPersonId));
            return;
        }
        sessionUser.setState(State.TRAVEL);
        if (data.endsWith(MULTI_PERSON_BUTTON_SUFFIX)) {
            botKeeper.getBot().editPhoto(botMenuService.preparePersonsListEditMessageMedia(messageId, data));
        } else {
            String[] components = data.split("_");
            botKeeper.getBot().editPhoto(botMenuService.preparePersonEditMessageMedia(messageId, components[2], defaultPersonId));
        }
    }

    private Long getChatId(Update update) {
        return update.getCallbackQuery().getMessage().getChatId();
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
