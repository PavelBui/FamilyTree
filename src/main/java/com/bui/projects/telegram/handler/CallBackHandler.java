package com.bui.projects.telegram.handler;

import com.bui.projects.dto.PersonDto;
import com.bui.projects.service.PersonService;
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
    private final PersonService personService;
    private final BotMenuService botMenuService;

    public CallBackHandler(BotKeeper botKeeper, PersonService personService, BotMenuService botMenuService) {
        this.botKeeper = botKeeper;
        this.personService = personService;
        this.botMenuService = botMenuService;
    }

    public void handle(Update update) {
        botMenuService.prepare(update);

        Long chatId = getChatId(update);
        SessionUser sessionUser = TelegramRequestContext.requestUser(chatId);

        String data = update.getCallbackQuery().getData();
        Integer messageId = getUpdateMessageId(update);
        Integer defaultPersonId = botKeeper.getTelegramBot().getDefaultPersonId();

        PersonDto personDtoByChatId = personService.getPersonByChatId(chatId);
        Integer homePersonId = personDtoByChatId != null ? personDtoByChatId.getId() : null;

        provideLog(chatId, data);
        if (HOME_BUTTON.equals(data)) {
            sessionUser.setState(State.HOME);
            PersonDto personDto = personService.getPersonByChatId(chatId);
            if (personDto != null) {
                sessionUser.setPersonId(personDto.getId());
                botKeeper.getBot().editPhoto(botMenuService.prepareHomePointEditMessageMedia(personDto, messageId, defaultPersonId));
            } else {
                botKeeper.getBot().executeMessage(botMenuService.prepareSendMessage(UNKNOWN_IDENTIFICATION_TEXT));
            }
            return;
        }
        if (DEFAULT_BUTTON.equals(data)) {
            sessionUser.setState(State.DEFAULT);
            sessionUser.setPersonId(defaultPersonId);
            PersonDto personDto = personService.getPerson(defaultPersonId);
            botKeeper.getBot().editPhoto(botMenuService.prepareDefaultPointEditMessageMedia(personDto, messageId, homePersonId));
            return;
        }
        if (data.startsWith(PHOTO_BUTTON_PREFIX)) {
            sessionUser.setState(State.PHOTO);
            PersonDto personDto = personService.getPerson(Integer.parseInt(data.replace(PHOTO_BUTTON_PREFIX, "")));
            List<Message> photoMessageList = botKeeper.getBot().sendMediaGroup(botMenuService.preparePhotosSendMediaGroup(personDto));
            botKeeper.getBot().executeMessage(botMenuService.preparePhotosSendMessage(personDto, photoMessageList));
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
            PersonDto personDto = personService.getPerson(Integer.parseInt(companents[0].replace(PHOTO_BACK_BUTTON_PREFIX, "")));
            botKeeper.getBot().editPhoto(botMenuService.preparePersonEditMessageMedia(personDto, messageId, homePersonId, defaultPersonId));
            return;
        }
        if (data.startsWith(PERSON_BUTTON_PREFIX)) {
            sessionUser.setState(State.TRAVEL);
            PersonDto personDto = personService.getPerson(Integer.parseInt(data.replace(PERSON_BUTTON_PREFIX, "")));
            sessionUser.setPersonId(personDto.getId());
            botKeeper.getBot().editPhoto(botMenuService.preparePersonEditMessageMedia(personDto, messageId, homePersonId, defaultPersonId));
            return;
        }
        sessionUser.setState(State.TRAVEL);
        if (data.endsWith(MULTI_PERSON_BUTTON_SUFFIX)) {
            Integer personId = Integer.parseInt(data
                    .replace(PARENTS.buttonPrefix(), "")
                    .replace(KIDS.buttonPrefix(), "")
                    .replace(SIBLINGS.buttonPrefix(), "")
                    .replace(SPOUSES.buttonPrefix(), "")
                    .replace(MULTI_PERSON_BUTTON_SUFFIX,""));
            PersonDto personDto = personService.getPerson(personId);
            sessionUser.setPersonId(personDto.getId());
            botKeeper.getBot().editPhoto(botMenuService.preparePersonsListEditMessageMedia(personDto, messageId, data, homePersonId));
        } else {
            String[] components = data.split("_");
            PersonDto personDto = personService.getPerson(Integer.parseInt(components[2]));
            sessionUser.setPersonId(personDto.getId());
            botKeeper.getBot().editPhoto(botMenuService.preparePersonEditMessageMedia(personDto, messageId, homePersonId, defaultPersonId));
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
