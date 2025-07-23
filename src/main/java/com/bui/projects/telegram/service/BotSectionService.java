package com.bui.projects.telegram.service;

import com.bui.projects.telegram.button.MarkupTemplates;
import com.bui.projects.telegram.session.SessionUser;
import com.bui.projects.telegram.session.Sessions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.File;
import java.util.Map;

@Slf4j
@Service
public class BotSectionService extends BaseService {

    public BotSectionService(Sessions sessions) {
        super(sessions);
    }

//    String getDefaultFileName() {
//        String tenantId = TenantUtils.getCurrentTenant();
//        return tenantId.toLowerCase().concat(".jpg");
//    }

    public SendPhoto sendSectionPhotoMessage(SessionUser sessionUser) {
        String messageText = "Test Message";
        File file = new File("");
//        String messageText = sectionService.getMainSectionContentByLang(sessionUser.getLan());
//        Lang lang = sessionUser.getLan();
//        SectionGetDTO rootSection = sectionService.getAllByParentId(0).getData().get(0);
//        File file = getSectionImage(lang, rootSection);
        InlineKeyboardMarkup inlineKeyboardMarkup = createMainSectionInlineKeyboard(sessionUser);

        return createSendPhoto(messageText, file, inlineKeyboardMarkup);
    }

    private InlineKeyboardMarkup createMainSectionInlineKeyboard(SessionUser sessionUser) {
//        SectionGetDTO rootSection = sectionService.getAllByParentId(0).getData().get(0);
//        List<SectionGetDTO> sections = sectionService.getAllByParentId(rootSection.getId()).getData();
//        Map<String, String> parentSections = sectionService.getAllByLangAsMap(sections, sessionUser.getLan());
        Map<String, String> parentSections = Map.of("Test", "Test");
        return MarkupTemplates.listEntityButtons(parentSections, 1);
    }

//    private File getSectionImage(Lang lang, SectionGetDTO sectionGetDTO) {
//        if (sectionGetDTO != null && sectionGetDTO.getAttachmentId(lang) != null) {
//            return attachmentService.getFileByAttachmentEntityLight(sectionGetDTO.getAttachmentId(lang));
//        } else {
//            return attachmentService.getFileByName(getDefaultFileName());
//        }
//    }

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
