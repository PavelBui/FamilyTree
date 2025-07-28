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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class BotMenuService extends BaseService {

    private final PersonService personService;

    public BotMenuService(Sessions sessions, PersonService personService) {
        super(sessions);
        this.personService = personService;
    }

    public SendPhoto sendStartPointMessage(SessionUser sessionUser) {
        try {
            File file = ResourceFileLoader.loadResourceAsTempFile("startimage.jpg");
            StringBuilder messageText = new StringBuilder();
            PersonDto personDto = personService.getPersonByChatId(sessionUser.getChatId());
            if (personDto != null) {
                messageText.append("Добро пожаловать, ").append(personDto.getLastName()).append(" ").append(personDto.getFirstName()).append(" ").append(personDto.getMiddleName()).append("!");
            } else {
                messageText.append("Добро пожаловать!");
            }
            messageText.append("\n").append("Нажмите на кнопку \"Найти себя\", чтобы найти себя в семейном древе используя Телеграм идентификатор, или нажмите на кнопку \"Персона по-умолчанию\", чтобы загрузить персону по-умолчанию и начать путешествие по семейному древу.");
            InlineKeyboardMarkup inlineKeyboardMarkup = createStartPointKeyboard();
            return createSendPhoto(messageText.toString(), file, inlineKeyboardMarkup);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public EditMessageMedia sendHomePointMessage(Integer messageId, SessionUser sessionUser, Integer defaultPersonId) throws IOException {
        PersonDto personDto = personService.getPersonByChatId(sessionUser.getChatId());
        if (personDto != null) {
            File tempFile = getPersonDefaultPhoto(personDto);
            String messageText = personDto.getFullDescription();
            InlineKeyboardMarkup inlineKeyboardMarkup = createPersonPointKeyboard(personDto, null, defaultPersonId);
            return createEditMessageMedia(messageId, messageText, tempFile, inlineKeyboardMarkup);
        }
        return null;
    }

    public SendPhoto sendNewHomePointMessage(SessionUser sessionUser, Integer defaultPersonId) throws IOException {
        PersonDto personDto = personService.getPersonByChatId(sessionUser.getChatId());
        if (personDto != null) {
            File tempFile = getPersonDefaultPhoto(personDto);
            String messageText = personDto.getFullDescription();
            InlineKeyboardMarkup inlineKeyboardMarkup = createPersonPointKeyboard(personDto, null, defaultPersonId);
            return createSendPhoto(messageText, tempFile, inlineKeyboardMarkup);
        }
        return null;
    }

    public EditMessageMedia sendDefaultPointMessage(SessionUser sessionUser, Integer messageId, Integer defaultPersonId) throws IOException {
        PersonDto personDtoByChatId = personService.getPersonByChatId(sessionUser.getChatId());
        PersonDto personDto = personService.getPerson(defaultPersonId);
        if (personDto != null) {
            File tempFile = getPersonDefaultPhoto(personDto);
            String messageText = personDto.getFullDescription();
            InlineKeyboardMarkup inlineKeyboardMarkup = createPersonPointKeyboard(personDto, personDtoByChatId.getId(), null);
            return createEditMessageMedia(messageId, messageText, tempFile, inlineKeyboardMarkup);
        }
        return null;
    }

    public EditMessageMedia sendPersonMessage(SessionUser sessionUser, Integer messageId, String stringId, Integer defaultPersonId) throws IOException {
        PersonDto personDtoByChatId = personService.getPersonByChatId(sessionUser.getChatId());
        PersonDto personDto = personService.getPerson(Integer.parseInt(stringId));
        if (personDto != null) {
            File tempFile = getPersonDefaultPhoto(personDto);
            String messageText = personDto.getFullDescription();
            InlineKeyboardMarkup inlineKeyboardMarkup = createPersonPointKeyboard(personDto, personDtoByChatId.getId(), defaultPersonId);
            return createEditMessageMedia(messageId, messageText, tempFile, inlineKeyboardMarkup);
        }
        return null;
    }

    public EditMessageMedia sendPersonsListMessage(Integer messageId, String personsType) throws IOException {
        Integer personId = Integer.parseInt(personsType.replace("go_parents", "").replace("go_kids", "").replace("go_siblings", "").replace("go_spouses", ""));
        PersonDto personDto = personService.getPerson(personId);
        if (personDto != null) {
            File tempFile = getPersonDefaultPhoto(personDto);
            String messageText = "";
            List<Integer> personList = new ArrayList<>();
            if (personsType.startsWith("go_parents")) {
                personList.addAll(personDto.getParentsIds());
                messageText = "Родители:";
            }
            if (personsType.startsWith("go_kids")) {
                personList.addAll(personDto.getKidsIds());
                messageText = "Дети:";
            }
            if (personsType.startsWith("go_siblings")) {
                personList.addAll(personDto.getSiblingsIds());
                messageText = "Братья и сестры:";
            }
            if (personsType.startsWith("go_spouses")) {
                personList.addAll(personDto.getSpousesIds());
                messageText = "Супруг(и):";
            }
            InlineKeyboardMarkup inlineKeyboardMarkup = createPersonListKeyboard(personList, personsType);
            return createEditMessageMedia(messageId, messageText, tempFile, inlineKeyboardMarkup);
        }
        return null;
    }

    private InlineKeyboardMarkup createStartPointKeyboard() {
        Map<String, String> parentSections = Map.of("go_home", "Найти себя", "go_default", "Персона по-умолчанию");
        return MarkupTemplates.listEntityButtons(parentSections, 1);
    }

    private InlineKeyboardMarkup createPersonPointKeyboard(PersonDto personDto, Integer homePersonId, Integer defaultPersonId) {
        Map<String, String> parentSections = new HashMap<>();
        if (!personDto.getParentsIds().isEmpty()) {
            parentSections.put("go_parents" + personDto.getId(), "Родители (" + personDto.getParentsIds().size() + ")");
        }
        if (!personDto.getKidsIds().isEmpty()) {
            parentSections.put("go_kids" + personDto.getId(), "Дети (" + personDto.getKidsIds().size() + ")");
        }
        if (!personDto.getSiblingsIds().isEmpty()) {
            parentSections.put("go_siblings" + personDto.getId(), "Братья и сестры (" + personDto.getSiblingsIds().size() + ")");
        }
        if (!personDto.getSpousesIds().isEmpty()) {
            parentSections.put("go_spouses" + personDto.getId(), "Супруг(и) (" + personDto.getSpousesIds().size() + ")");
        }
        if (homePersonId != null && !homePersonId.equals(personDto.getId())) {
            parentSections.put("go_home", "Вернуться к себе");
        }
        if (defaultPersonId != null && !defaultPersonId.equals(personDto.getId())) {
            parentSections.put("go_default", "Персона по-умолчанию");
        }
        return MarkupTemplates.listEntityButtons(parentSections, 2);
    }

    private InlineKeyboardMarkup createPersonListKeyboard(List<Integer> personList, String personsType) {
        Map<String, String> parentSections = new HashMap<>();
        for (int personId : personList) {
            PersonDto personDto = personService.getPerson(personId);
            parentSections.put("person_" + personId, personDto.getFullName(personsType));
        }
        parentSections.put("go_home", "Вернуться к себе");
        parentSections.put("go_default", "Персона по-умолчанию");
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

    private EditMessageMedia createEditMessageMedia(Integer messageId, String messageText, File file, InlineKeyboardMarkup inlineKeyboardMarkup) {
        InputMedia inputMedia = new InputMediaPhoto();
        inputMedia.setMedia(file, file.getName());
        inputMedia.setCaption(messageText);
        inputMedia.setParseMode("HTML");
        EditMessageMedia editMessageMedia = new EditMessageMedia(chatId.toString(), messageId, null, inputMedia, inlineKeyboardMarkup);
        editMessageMedia.setChatId(chatId);
        return editMessageMedia;
    }

    private File getPersonDefaultPhoto(PersonDto personDto) throws IOException {
        if (personDto.getDefaultPhotoId() != null) {
            PhotoDto photoDto = personService.getPhoto(personDto.getId(), personDto.getDefaultPhotoId());
            File tempFile = File.createTempFile("temp_", "_" + personDto.getLastName() + personDto.getLastName());
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(photoDto.getPhotoBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return tempFile;
        } else {
            return ResourceFileLoader.loadResourceAsTempFile("defaultphoto.jpg");
        }
    }
}
