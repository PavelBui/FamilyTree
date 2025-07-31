package com.bui.projects.telegram.service;

import com.bui.projects.dto.PersonDto;
import com.bui.projects.dto.PhotoDto;
import com.bui.projects.service.PersonService;
import com.bui.projects.telegram.button.MarkupTemplates;
import com.bui.projects.telegram.session.Sessions;
import com.bui.projects.telegram.util.Constants;
import com.bui.projects.util.ResourceFileLoader;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static com.bui.projects.telegram.util.Constants.*;

@Slf4j
@Service
public class BotMenuService extends BaseService {

    private final PersonService personService;

    public BotMenuService(Sessions sessions, PersonService personService) {
        super(sessions);
        this.personService = personService;
    }

    @SneakyThrows
    public SendPhoto prepareStartPointSendMessage() {
        File file = ResourceFileLoader.loadResourceAsTempFile(START_IMAGE_FILE);
        StringBuilder messageText = new StringBuilder();
        PersonDto personDto = personService.getPersonByChatId(chatId);
        if (personDto != null) {
            messageText.append(WELCOME_PERSON_TEXT).append(personDto.getLastName()).append(" ").append(personDto.getFirstName()).append(" ").append(personDto.getMiddleName()).append("!");
        } else {
            messageText.append(WELCOME_UNKNOWN_TEXT);
        }
        messageText.append("\n").append(INTRO_TEXT);
        InlineKeyboardMarkup inlineKeyboardMarkup = createStartPointKeyboard();
        return createSendPhoto(messageText.toString(), file, inlineKeyboardMarkup);
    }

    public SendPhoto prepareHomePointSendPhoto(Integer defaultPersonId) {
        PersonDto personDto = personService.getPersonByChatId(chatId);
        if (personDto != null) {
            File tempFile = getPersonPhotoFile(personDto, personDto.getDefaultPhotoId());
            String messageText = personDto.getFullDescription();
            InlineKeyboardMarkup inlineKeyboardMarkup = createPersonPointKeyboard(personDto, null, defaultPersonId);
            return createSendPhoto(messageText, tempFile, inlineKeyboardMarkup);
        }
        return null;
    }

    public EditMessageMedia prepareHomePointEditMessageMedia(Integer messageId, Integer defaultPersonId) {
        PersonDto personDto = personService.getPersonByChatId(chatId);
        if (personDto != null) {
            File tempFile = getPersonPhotoFile(personDto, personDto.getDefaultPhotoId());
            String messageText = personDto.getFullDescription();
            InlineKeyboardMarkup inlineKeyboardMarkup = createPersonPointKeyboard(personDto, null, defaultPersonId);
            return createEditMessageMedia(messageId, messageText, tempFile, inlineKeyboardMarkup);
        }
        return null;
    }

    public EditMessageMedia prepareDefaultPointEditMessageMedia(Integer messageId, Integer defaultPersonId) {
        PersonDto personDtoByChatId = personService.getPersonByChatId(chatId);
        PersonDto personDto = personService.getPerson(defaultPersonId);
        if (personDto != null) {
            File tempFile = getPersonPhotoFile(personDto, personDto.getDefaultPhotoId());
            String messageText = personDto.getFullDescription();
            InlineKeyboardMarkup inlineKeyboardMarkup = createPersonPointKeyboard(personDto, personDtoByChatId.getId(), null);
            return createEditMessageMedia(messageId, messageText, tempFile, inlineKeyboardMarkup);
        }
        return null;
    }

    public EditMessageMedia preparePersonEditMessageMedia(Integer messageId, String stringId, Integer defaultPersonId) {
        PersonDto personDtoByChatId = personService.getPersonByChatId(chatId);
        PersonDto personDto = personService.getPerson(Integer.parseInt(stringId));
        if (personDto != null) {
            File tempFile = getPersonPhotoFile(personDto, personDto.getDefaultPhotoId());
            String messageText = personDto.getFullDescription();
            InlineKeyboardMarkup inlineKeyboardMarkup = createPersonPointKeyboard(personDto, personDtoByChatId.getId(), defaultPersonId);
            return createEditMessageMedia(messageId, messageText, tempFile, inlineKeyboardMarkup);
        }
        return null;
    }

    public EditMessageMedia preparePersonsListEditMessageMedia(Integer messageId, String personsType) {
        Integer personId = Integer.parseInt(personsType
                .replace(PARENTS.buttonPrefix(), "")
                .replace(KIDS.buttonPrefix(), "")
                .replace(SIBLINGS.buttonPrefix(), "")
                .replace(SPOUSES.buttonPrefix(), "")
                .replace(MULTI_PERSON_BUTTON_SUFFIX,""));
        PersonDto personDto = personService.getPerson(personId);
        if (personDto != null) {
            File tempFile = getPersonPhotoFile(personDto, personDto.getDefaultPhotoId());
            String messageText = "";
            List<Integer> personList = new ArrayList<>();
            if (personsType.startsWith(PARENTS.buttonPrefix())) {
                personList.addAll(personDto.getParentsIds());
                messageText = PARENTS.list();
            }
            if (personsType.startsWith(KIDS.buttonPrefix())) {
                personList.addAll(personDto.getKidsIds());
                messageText = KIDS.list();
            }
            if (personsType.startsWith(SIBLINGS.buttonPrefix())) {
                personList.addAll(personDto.getSiblingsIds());
                messageText = SIBLINGS.list();
            }
            if (personsType.startsWith(SPOUSES.buttonPrefix())) {
                personList.addAll(personDto.getSpousesIds());
                messageText = SPOUSES.list();
            }
            InlineKeyboardMarkup inlineKeyboardMarkup = createPersonListKeyboard(personList, personsType);
            return createEditMessageMedia(messageId, messageText, tempFile, inlineKeyboardMarkup);
        }
        return null;
    }

    public SendMediaGroup preparePhotosSendMediaGroup(String stringId) {
        Integer personId = Integer.parseInt(stringId);
        PersonDto personDto = personService.getPerson(personId);
        if (personDto != null) {
            List<File> tempFiles = new ArrayList<>();
            personDto.getPhotoIds().stream()
                    .limit(10)
                    .forEach(photoId -> tempFiles.add(getPersonPhotoFile(personDto, photoId)));
            return createMediaGroupMessage(tempFiles);
        }
        return null;
    }

    public DeleteMessage prepareDeleteMessage(Integer messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);
        return deleteMessage;
    }

    public SendMessage prepareSendMessage(String stringId, List<Message> photoMessageList) {
        PersonDto personDto = personService.getPerson(Integer.parseInt(stringId));
        if (personDto != null) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Выберите действие:");

            StringBuilder photoMessageIds = new StringBuilder();
            photoMessageList.stream()
                    .forEach(message -> photoMessageIds.append("#").append(message.getMessageId()));

            Map<String, String> parentSections = new HashMap<>();
            parentSections.put(PHOTO_BACK_BUTTON_PREFIX + personDto.getId() + photoMessageIds, "Вернуться");
            InlineKeyboardMarkup inlineKeyboardMarkup = MarkupTemplates.listEntityButtons(parentSections, 1);
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            return sendMessage;
        }
        return null;
    }

    private InlineKeyboardMarkup createStartPointKeyboard() {
        Map<String, String> parentSections = Map.of(HOME_BUTTON, FIND_HOME_TEXT, DEFAULT_BUTTON, DEFAULT_TEXT);
        return MarkupTemplates.listEntityButtons(parentSections, 1);
    }

    private InlineKeyboardMarkup createPersonPointKeyboard(PersonDto personDto, Integer homePersonId, Integer defaultPersonId) {
        Map<String, String> parentSections = new HashMap<>();
        checkRelationshipList(personDto.getParentsIds(), personDto.getId(), PARENTS)
                .ifPresent(entry -> parentSections.put(entry.getKey(), entry.getValue()));
        checkRelationshipList(personDto.getKidsIds(), personDto.getId(), KIDS)
                .ifPresent(entry -> parentSections.put(entry.getKey(), entry.getValue()));
        checkRelationshipList(personDto.getSiblingsIds(), personDto.getId(), SIBLINGS)
                .ifPresent(entry -> parentSections.put(entry.getKey(), entry.getValue()));
        checkRelationshipList(personDto.getSpousesIds(), personDto.getId(), SPOUSES)
                .ifPresent(entry -> parentSections.put(entry.getKey(), entry.getValue()));
        if (personDto.getPhotoIds() != null && personDto.getPhotoIds().size() > 1) {
            parentSections.put(PHOTO_BUTTON_PREFIX + personDto.getId(), PHOTO_TEXT);
        }
        if (homePersonId != null && !homePersonId.equals(personDto.getId())) {
            parentSections.put(HOME_BUTTON, HOME_TEXT);
        }
        if (defaultPersonId != null && !defaultPersonId.equals(personDto.getId())) {
            parentSections.put(DEFAULT_BUTTON, DEFAULT_TEXT);
        }
        return MarkupTemplates.listEntityButtons(parentSections, 2);
    }

    private InlineKeyboardMarkup createPersonListKeyboard(List<Integer> personList, String personsType) {
        Map<String, String> parentSections = new HashMap<>();
        for (int personId : personList) {
            PersonDto personDto = personService.getPerson(personId);
            parentSections.put(PERSON_BUTTON_PREFIX + personId, personDto.getFullName(personsType));
        }
        parentSections.put(HOME_BUTTON, HOME_TEXT);
        parentSections.put(DEFAULT_BUTTON, DEFAULT_TEXT);
        return MarkupTemplates.listEntityButtons(parentSections, 1);
    }

    private Optional<Map.Entry<String, String>> checkRelationshipList(List<Integer> relationshipIds, Integer personId, Constants.RelationshipGroup constants) {
        if (relationshipIds == null || relationshipIds.isEmpty()) {
            return Optional.empty();
        }
        String key;
        String value;
        if (relationshipIds.size() == 1) {
            Integer linkPersonId = relationshipIds.get(0);
            PersonDto linkPersonDto = personService.getPerson(linkPersonId);
            key = constants.buttonPrefix() + linkPersonId;
            value = linkPersonDto.isMale() ? constants.male() : constants.female();
        } else {
            key = constants.buttonPrefix() + personId + MULTI_PERSON_BUTTON_SUFFIX;
            if (isAllMale(relationshipIds)) {
                value = constants.males() + " (" + relationshipIds.size() + ")";
            } else if (isAllFemale(relationshipIds)) {
                value = constants.females() + " (" + relationshipIds.size() + ")";
            } else {
                value = constants.list() + " (" + relationshipIds.size() + ")";
            }
        }
        return Optional.of(new AbstractMap.SimpleEntry<>(key, value));
    }

    private boolean isAllMale(List<Integer> relationshipIds) {
        for (Integer id : relationshipIds) {
            PersonDto personDto = personService.getPerson(id);
            if (!personDto.isMale()) {
                return false;
            }
        }
        return true;
    }

    private boolean isAllFemale(List<Integer> relationshipIds) {
        for (Integer id : relationshipIds) {
            PersonDto personDto = personService.getPerson(id);
            if (personDto.isMale()) {
                return false;
            }
        }
        return true;
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

    private SendMediaGroup createMediaGroupMessage(List<File> files) {
        List<InputMedia> inputMediaList = new ArrayList<>();
        for (File file : files) {
            InputMedia inputMedia = new InputMediaPhoto();
            inputMedia.setMedia(file, file.getName());
            inputMediaList.add(inputMedia);
        }
        SendMediaGroup sendMediaGroup = new SendMediaGroup(chatId.toString(), inputMediaList);
        sendMediaGroup.setChatId(chatId);
        sendMediaGroup.setProtectContent(true);
        return sendMediaGroup;
    }

    @SneakyThrows
    private File getPersonPhotoFile(PersonDto personDto, Integer photoId) {
        if (personDto.getDefaultPhotoId() != null) {
            PhotoDto photoDto = personService.getPhoto(personDto.getId(), photoId);
            File tempFile = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX + personDto.getLastName() + personDto.getLastName());
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(photoDto.getPhotoBytes());
            } catch (IOException e) {
                throw new IOException(e);
            }
            return tempFile;
        } else {
            return ResourceFileLoader.loadResourceAsTempFile(DEFAULT_PHOTO_FILE);
        }
    }
}
