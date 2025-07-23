package com.bui.projects.telegram.handler;

import com.bui.projects.telegram.BotKeeper;
import com.bui.projects.telegram.service.BotSectionService;
import com.bui.projects.telegram.service.BotService;
import com.bui.projects.telegram.session.SessionUser;
import com.bui.projects.telegram.session.TelegramRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Component
@Slf4j
public class CallBackHandler extends BaseMethods implements IBaseHandler {

    private final BotService botService;
    private final BotSectionService sectionService;

    public CallBackHandler(BotKeeper botKeeper, BotService botService, BotSectionService sectionService) {
        super(botKeeper);
        this.botService = botService;
        this.sectionService = sectionService;
    }

    @Override
    public void handle(Update update) {
        try {
            addUserIdToLogs(update);
            sectionService.prepare(update);
            botService.prepare(update);

            Long chatId = getChatId(update);
            SessionUser sessionUser = TelegramRequestContext.requestUser(chatId);

            checkSpecialMessage(update);

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

    private void addUserIdToLogs(Update update) {
        String chatId = Optional.ofNullable(update.getCallbackQuery())
                .map(CallbackQuery::getMessage)
                .map(MaybeInaccessibleMessage::getChatId)
                .map(Object::toString).orElse("Undefined");
        MDC.put("chatId", chatId);
        MDC.put("typeId", "callback");
    }
}
