package com.bui.projects.telegram.service;

import com.bui.projects.dto.PersonDto;
import com.bui.projects.dto.PhotoDto;
import com.bui.projects.service.PersonService;
import com.bui.projects.telegram.button.MarkupTemplates;
import com.bui.projects.telegram.session.SessionUser;
import com.bui.projects.telegram.session.Sessions;
import com.bui.projects.util.ResourceFileLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
public class BotMenuService extends BaseService {

    private final PersonService personService;

    public BotMenuService(Sessions sessions, PersonService personService) {
        super(sessions);
        this.personService = personService;
    }

    public SendPhoto sendStartPointMessage() {
        try {
            File file = ResourceFileLoader.loadResourceAsTempFile("startimage.jpg");
            String messageText =  "Нажмите на кнопку \"Найти себя\", чтобы найти себя в семейном древе используя идентификатор Телеграма, или нажмите на кнопку \"Персона по-умолчанию\", чтобы загрузить персону по-умолчанию и начать путешествие по семейному древу.";
            InlineKeyboardMarkup inlineKeyboardMarkup = createStartPointKeyboard();
            return createSendPhoto(messageText, file, inlineKeyboardMarkup);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SendPhoto sendHomePointMessage(SessionUser sessionUser) {
        PersonDto personDto = personService.getPersonByChatId(sessionUser.getChatId());
        if (personDto != null) {
            PhotoDto photoDto = personService.getPhoto(personDto.getId(), personDto.getDefaultPhotoId());
            File file = new File(sessionUser.getLastname() + " " + sessionUser.getLastname());
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(photoDto.getPhotoBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String messageText = "Test Message";
            InlineKeyboardMarkup inlineKeyboardMarkup = createStartPointKeyboard();
            return createSendPhoto(messageText, file, inlineKeyboardMarkup);
        }
        return null;
    }

    private InlineKeyboardMarkup createStartPointKeyboard() {
        Map<String, String> parentSections = Map.of("go_home", "Найти себя", "go_default", "Персона по-умолчанию");
        return MarkupTemplates.listEntityButtons(parentSections, 1);
    }

    private SendPhoto createSendPhoto(String messageText, File file, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(messageText);
        InputFile inputFile = new InputFile();
        inputFile.setMedia(file);
        sendPhoto.setPhoto(inputFile);
        sendPhoto.setParseMode("HTML");
        sendPhoto.setReplyMarkup(inlineKeyboardMarkup);
        return sendPhoto;
    }
}
