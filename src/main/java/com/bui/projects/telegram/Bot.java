package com.bui.projects.telegram;

import com.bui.projects.telegram.config.TelegramBotProperties;
import com.bui.projects.telegram.handler.UpdateHandler;
import com.bui.projects.telegram.session.RequestContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {

    private final UpdateHandler handler;
    private final RequestContext requestContext;
    private final TelegramBotProperties telegramBotProperties;

    @Override
    public String getBotUsername() {
        return telegramBotProperties.getUrl();
    }

    @Override
    public String getBotToken() {
        return telegramBotProperties.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        requestContext.authenticate(update);
        handler.handle(update);
        requestContext.refresh(update);
    }

    public void executeMessage(BotApiMethod<?> message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Message executing failed {}", e.getMessage());
        }
    }

    public Message executeMessageWithReturn(BotApiMethod<?> message) {
        try {
            return (Message) execute(message);
        } catch (TelegramApiException e) {
            log.error("Message with return executing failed {}", e.getMessage());
        }
        return null;
    }

    public void sendAudio(SendAudio message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Sending audio failed {}", e.getMessage());
        }
    }

    public void sendMediaGroup(SendMediaGroup message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Media executing failed {}", e.getMessage());
        }
    }

    public void editPhoto(EditMessageMedia editMessageMedia) {
        try {
            execute(editMessageMedia);
        } catch (TelegramApiException ex) {
            log.error("Edit message executing failed {}", ex.getMessage());
        }
    }

    public void sendPhoto(SendPhoto photo) {
        try {
            execute(photo);
        } catch (TelegramApiException e) {
            log.error("Send photo executing failed {}", e.getMessage());
        }
    }

    public void sendAnimation(SendAnimation message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Sending animation failed {}", e.getMessage());
        }
    }

    public void sendLocation(SendLocation location) {
        try {
            execute(location);
        } catch (TelegramApiException e) {
            log.error("Sending location failed {}", e.getMessage());
        }
    }

    public Integer sendLocationWithReturn(SendLocation location) {
        try {
            Message execute = execute(location);
            return execute.getMessageId();
        } catch (TelegramApiException e) {
            log.error("Sending location failed {}", e.getMessage());
        }
        return null;
    }

    public void deleteMessage(DeleteMessage deleteMessage) {
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            log.error("Deleting message failed {}", e.getMessage());
        }
    }

    public void sendVideo(SendVideo video) {
        try {
            execute(video);
        } catch (TelegramApiException e) {
            log.error("Sending video failed {}", e.getMessage());
        }
    }

    public void sendVoice(SendVoice video) {
        try {
            execute(video);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void send(SendDocument sendDocument) {
        try {
            this.execute(sendDocument);
        } catch (TelegramApiException e) {
            log.error("Sending document failed {}", e.getMessage());
        }
    }

    public void sendSticker(SendSticker poll) {
        try {
            this.execute(poll);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendChatAction(SendChatAction action) {
        try {
            this.execute(action);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
