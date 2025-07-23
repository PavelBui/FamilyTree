package com.bui.projects.telegram.service;

import com.bui.projects.telegram.session.Sessions;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public abstract class BaseService {

    protected Long chatId;

    protected final Sessions sessions;

    public BaseService(Sessions sessions) {
        this.sessions = sessions;
    }

    public void prepare(Update update) {
        MaybeInaccessibleMessage message;
        if (update.hasCallbackQuery()) {
            message = update.getCallbackQuery().getMessage();
        } else {
            message = update.getMessage();
        }
        chatId = message.getChatId();
    }
}
