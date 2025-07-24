package com.bui.projects.telegram.handler;

import com.bui.projects.telegram.BotKeeper;
import com.bui.projects.telegram.service.BotMenuService;
import com.bui.projects.telegram.service.BotService;
import com.bui.projects.telegram.session.SessionUser;
import com.bui.projects.telegram.session.TelegramRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class CallBackHandler extends BaseMethods implements IBaseHandler {

    private final BotService botService;
    private final BotMenuService sectionService;

    public CallBackHandler(BotKeeper botKeeper, BotService botService, BotMenuService sectionService) {
        super(botKeeper);
        this.botService = botService;
        this.sectionService = sectionService;
    }

    @Override
    public void handle(Update update) {
        try {
            sectionService.prepare(update);
            botService.prepare(update);

            Long chatId = getChatId(update);
            SessionUser sessionUser = TelegramRequestContext.requestUser(chatId);

            checkSpecialMessage(update);
            switch (sessionUser.getState()) {
//                case START -> selectLanguageCallbackHandler.handle(update);
//                case HOME -> selectCountryCallbackHandler.handle(update);
//                case TRAVEL -> selectRegionCallBackHandler.handle(update);
//                case DEFAULT -> changeLanguageCallbackHandler.handle(update);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private Long getChatId(Update update) {
        return update.getCallbackQuery().getMessage().getChatId();
    }

    private void checkSpecialMessage(Update update) {
        Long chatId = getChatId(update);
        SessionUser sessionUser = TelegramRequestContext.requestUser(chatId);

//        String data = update.getCallbackQuery().getData();
//        if (data.startsWith("cancel")) {
//            deleteMessage(update.getCallbackQuery().getMessage().getChatId(), update.getCallbackQuery().getMessage().getMessageId());
//        } else if (data.contains("read")) {
//            familyTreeBot.executeMessage(editMsgObject(botService.sendPostViewedMessage(update, sessionUser), update));
//        }
    }
}
