package com.bui.projects.telegram.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UpdateHandler {

    private final MessageHandler messageHandler;
    private final CallBackHandler callbackHandler;

    public UpdateHandler(MessageHandler messageHandler, CallBackHandler callbackHandler) {
        this.messageHandler = messageHandler;
        this.callbackHandler = callbackHandler;
    }

    public void handle(Update update) {
        if (update.hasMessage()) {
            messageHandler.handle(update);
        } else if (update.hasCallbackQuery()) callbackHandler.handle(update);
    }
}
