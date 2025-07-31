package com.bui.projects.telegram.handler;

import com.bui.projects.dto.PersonDto;
import com.bui.projects.service.PersonService;
import com.bui.projects.telegram.BotKeeper;
import com.bui.projects.telegram.service.BotMenuService;
import com.bui.projects.telegram.session.SessionUser;
import com.bui.projects.telegram.session.TelegramRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.bui.projects.telegram.util.Constants.*;


@Slf4j
@Component
public class MessageHandler {

    public final BotKeeper botKeeper;
    private final PersonService personService;
//    private final BotReplyKeyboardService botReplyKeyboardService;
    private final BotMenuService botMenuService;

    public MessageHandler(
            BotKeeper botKeeper,
            PersonService personService,
//            BotReplyKeyboardService botReplyKeyboardService,
            BotMenuService botMenuService) {
        this.botKeeper = botKeeper;
        this.personService = personService;
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
            PersonDto personDto = personService.getPersonByChatId(chatId);
            botKeeper.getBot().sendPhoto(botMenuService.prepareStartPointSendMessage(personDto));
            return;
        }
        switch (messageText) {
            case HOME_COMMAND -> goHome(chatId);
            case ME_COMMAND -> definePerson(chatId);
        }
    }

    private void goHome(Long chatId) {
        PersonDto personDto = personService.getPersonByChatId(chatId);
        Integer defaultPersonId = botKeeper.getTelegramBot().getDefaultPersonId();
        SessionUser sessionUser = TelegramRequestContext.requestUser(chatId);
        if (personDto != null) {
            sessionUser.setPersonId(personDto.getId());
            botKeeper.getBot().sendPhoto(botMenuService.preparePersonSendPhoto(personDto, defaultPersonId));
        } else {
            botKeeper.getBot().executeMessage(botMenuService.prepareSendMessage(UNKNOWN_IDENTIFICATION_TEXT));
        }
    }

    private void definePerson(Long chatId) {
        SessionUser sessionUser = TelegramRequestContext.requestUser(chatId);
        PersonDto personDto = personService.getPersonByChatId(chatId);
        if (personDto != null) {
            botKeeper.getBot().executeMessage(botMenuService.prepareSendMessage(WRONG_IDENTIFICATION_TEXT_PREFIX + personDto.getShortName() + WRONG_IDENTIFICATION_TEXT_SUFFIX));
        } else {
            Integer defaultPersonId = botKeeper.getTelegramBot().getDefaultPersonId();
            PersonDto realPersonDto = personService.getPerson(sessionUser.getPersonId());
            realPersonDto.setChatId(chatId);
            personService.updatePerson(realPersonDto.getId(), realPersonDto);
            botKeeper.getBot().executeMessage(botMenuService.prepareSendMessage(SUCCESS_IDENTIFICATION_TEXT + realPersonDto.getShortName() + "!"));
            botKeeper.getBot().sendPhoto(botMenuService.preparePersonSendPhoto(realPersonDto, defaultPersonId));
        }
    }

    private Long getChatId(Update update) {
        return update.getMessage().getChatId();
    }

    private void provideLog(Long chatId, String messageText) {
        log.info("ChatId: {}. Command {}", chatId, messageText);
    }
}
