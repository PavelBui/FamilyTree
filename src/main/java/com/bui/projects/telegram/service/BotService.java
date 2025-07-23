package com.bui.projects.telegram.service;

import com.bui.projects.service.AccountService;
import com.bui.projects.telegram.session.Sessions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
public class BotService extends BaseService {

    public BotService(Sessions sessions) {
        super(sessions);
    }

//  //  Step 1
//  @Transactional
//  public SendMessage sendLanMessage(Lang lang) {
//    String resultSkipMessage;
//    String resultTextMessage;
//    List<SettingEntity> lansEntities = settingsService.getSettingsByKey(BOT_LANS).stream()
//       .sorted(Comparator.comparing(SettingEntity::getLanguageId)).toList();
//    if (lang != null) {
//        resultSkipMessage = settingsService.getSettingByKeyAndLanguage(BOT_MESSAGE_SKIP, lang);
//        resultTextMessage = settingsService.getSettingByKeyAndLanguage(BOT_MESSAGE_LANS, lang) + " 🔽";
//    } else {
//        List<SettingEntity> selectLansEntities = settingsService.getSettingsByKey(BOT_MESSAGE_LANS).stream()
//                .sorted(Comparator.comparing(SettingEntity::getLanguageId)).toList();
//        List<SettingEntity> skipLansEntities = settingsService.getSettingsByKey(BOT_MESSAGE_SKIP).stream()
//                .sorted(Comparator.comparing(SettingEntity::getLanguageId)).toList();
//        StringBuilder messageText = new StringBuilder();
//        StringBuilder skipText = new StringBuilder();
//        for (int i = 0; i < lansEntities.size() - 1; i++) {
//            messageText.append(selectLansEntities.get(i).getValue()).append(" / ");
//            skipText.append(skipLansEntities.get(i).getValue()).append(" / ");
//        }
//        messageText.append(selectLansEntities.get(selectLansEntities.size() - 1).getValue()).append(" 🔽");
//        skipText.append(skipLansEntities.get(skipLansEntities.size() - 1).getValue());
//        resultSkipMessage = skipText.toString();
//        resultTextMessage = messageText.toString();
//    }
//    List<String> buttonList = new ArrayList<>();
//    for (int i = 0; i < lansEntities.size() - 1; i++) {
//        buttonList.add(lansEntities.get(i).getValue());
//    }
//    buttonList.add(lansEntities.get(lansEntities.size() - 1).getValue());
//    InlineKeyboardMarkup inlineKeyboardMarkup = MarkupTemplates.listButtons(lansEntities.size(), buttonList);
//    MarkupTemplates.addButton(inlineKeyboardMarkup, SKIP_BUTTON_ID, resultSkipMessage);
//    SendMessage sendMessage = new SendMessage(chatId.toString(), resultTextMessage);
//    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
//    return sendMessage;
//  }
//
//  //  Step 3
//  public SendMessage sendCountryMessage(SessionUser sessionUser) {
//    Map<String, String> countries = countryService.getAllByLan(sessionUser.getLan());
//    String skipText = settingsService.getSettingByKeyAndLanguage(BOT_MESSAGE_SKIP, sessionUser.getLan());
//
//    sessions.setState(State.SELECT_COUNTRY, chatId);
//    InlineKeyboardMarkup inlineKeyboardMarkup = MarkupTemplates.listEntityButtons(countries, 3);
//    String messageText = settingsService.getSettingByKeyAndLanguage(BOT_MESSAGE_COUNTRY, sessionUser.getLan());
//    MarkupTemplates.addButton(inlineKeyboardMarkup, SKIP_BUTTON_ID, skipText);
//
//    SendMessage sendMessage = new SendMessage(chatId.toString(), messageText);
//    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
//    return sendMessage;
//  }
//
//    //  Step 4
//    @Transactional
//    public SendMessage sendRegionMessage(SessionUser sessionUser, Integer countryId) {
//        String messageText = settingsService.getSettingByKeyAndLanguage(BOT_MESSAGE_REGION, sessionUser.getLan());
//        String skipText = settingsService.getSettingByKeyAndLanguage(BOT_MESSAGE_SKIP, sessionUser.getLan());
//
//        Map<String, String> regions = regionService.getAllByLan(regionService.getAllByCountryId(countryId), sessionUser.getLan());
//        InlineKeyboardMarkup inlineKeyboardMarkup = MarkupTemplates.listEntityButtons(regions, 2);
//        MarkupTemplates.addButton(inlineKeyboardMarkup, SKIP_BUTTON_ID, skipText);
//
//        SendMessage sendMessage = new SendMessage(chatId.toString(), messageText);
//        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
//        return sendMessage;
//    }

//    @Transactional
//    public SendMessage sendRegionMessage(SessionUser sessionUser) {
//        return sendRegionMessage(sessionUser, getDefaultCountryId());
//    }

//  public SendMessage sendPostViewedMessage(Update update, SessionUser sessionUser) {
//    String data = update.getCallbackQuery().getData();
//    StringTokenizer stringTokenizer = new StringTokenizer(data, "##");
//    stringTokenizer.nextToken();
//    Integer postId = Integer.parseInt(stringTokenizer.nextToken());
//    PostGetDTO post = postService.get(postId).getData();
//    postService.addViewer(postId, chatId);
//    String messageContent = post.getContentEn();
//    String messageTitle = post.getTitleEn();
//    switch (sessionUser.getLan()) {
//      case UZ -> {
//        messageContent = post.getContentUz();
//        messageTitle = post.getTitleUz();
//      }
//      case RU -> {
//        messageContent = post.getContentRu();
//        messageTitle = post.getTitleUz();
//      }
//    }
//    String messageText = messageTitle.concat("\n\n").concat(messageContent);
//    return new SendMessage(chatId.toString(), messageText);
//  }
//
//    public List<CountryGetDTO> getCountries() {
//        return countryService.getAll().getData();
//    }
//
//    public Integer getDefaultCountryId() {
//        return countryService.getDefaultCountryId();
//    }
}
