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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public SendPhoto sendHomePointMessage(SessionUser sessionUser) throws IOException {
        PersonDto personDto = personService.getPersonByChatId(sessionUser.getChatId());
        if (personDto != null) {
            PhotoDto photoDto = personService.getPhoto(personDto.getId(), personDto.getDefaultPhotoId());
            File tempFile = File.createTempFile("temp_", "_" + sessionUser.getLastname() + sessionUser.getLastname());
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(photoDto.getPhotoBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String messageText = personDto.getFullDescription();
            InlineKeyboardMarkup inlineKeyboardMarkup = createPersonPointKeyboard(personDto);
            return createSendPhoto(messageText, tempFile, inlineKeyboardMarkup);
        }
        return null;
    }

    public SendPhoto sendDefaultPointMessage(Integer defaultPersonId) throws IOException {
        PersonDto personDto = personService.getPerson(defaultPersonId);
        if (personDto != null) {
            PhotoDto photoDto = personService.getPhoto(personDto.getId(), personDto.getDefaultPhotoId());
            File tempFile = File.createTempFile("temp_", "_" + personDto.getLastName() + personDto.getFirstName());
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(photoDto.getPhotoBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String messageText = personDto.getFullDescription();
            InlineKeyboardMarkup inlineKeyboardMarkup = createPersonPointKeyboard(personDto);
            return createSendPhoto(messageText, tempFile, inlineKeyboardMarkup);
        }
        return null;
    }

    public SendPhoto sendPersonMessage(String stringId) throws IOException {
        PersonDto personDto = personService.getPerson(Integer.parseInt(stringId));
        if (personDto != null) {
            PhotoDto photoDto = personService.getPhoto(personDto.getId(), personDto.getDefaultPhotoId());
            File tempFile = File.createTempFile("temp_", "_" + personDto.getLastName() + personDto.getFirstName());
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(photoDto.getPhotoBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String messageText = personDto.getFullDescription();
            InlineKeyboardMarkup inlineKeyboardMarkup = createPersonPointKeyboard(personDto);
            return createSendPhoto(messageText, tempFile, inlineKeyboardMarkup);
        }
        return null;
    }

    public SendPhoto sendPersonsListMessage(String personsType) throws IOException {
        Integer personId = Integer.parseInt(personsType.replace("go_parents", "").replace("go_kids", "").replace("go_siblings", "").replace("go_spouses", ""));
        PersonDto personDto = personService.getPerson(personId);
        if (personDto != null) {
            PhotoDto photoDto = personService.getPhoto(personDto.getId(), personDto.getDefaultPhotoId());
            File tempFile = File.createTempFile("temp_", "_" + personDto.getLastName() + personDto.getLastName());
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(photoDto.getPhotoBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
            return createSendPhoto(messageText, tempFile, inlineKeyboardMarkup);
        }
        return null;
    }

    private InlineKeyboardMarkup createStartPointKeyboard() {
        Map<String, String> parentSections = Map.of("go_home", "Найти себя", "go_default", "Персона по-умолчанию");
        return MarkupTemplates.listEntityButtons(parentSections, 1);
    }

    private InlineKeyboardMarkup createPersonPointKeyboard(PersonDto personDto) {
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
        parentSections.put("go_home", "Вернуться к себе");
        parentSections.put("go_default", "Персона по-умолчанию");
        return MarkupTemplates.listEntityButtons(parentSections, 1);
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
}
