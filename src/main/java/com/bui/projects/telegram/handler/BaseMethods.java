package com.bui.projects.telegram.handler;

import com.bui.projects.telegram.BotKeeper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import static java.lang.Math.toIntExact;

@Component
@RequiredArgsConstructor
public class BaseMethods {
    public final BotKeeper botKeeper;

    public SendMessage msgObject(long chatId, String text) {
        SendMessage sendMessage = new SendMessage(chatId + "", text);
        sendMessage.enableHtml(true);
        return sendMessage;
    }

    public EditMessageText editMsgObject(SendMessage sendMessage, Update update) {
        long message_id;
        if (update.hasCallbackQuery()) {
            message_id = update.getCallbackQuery().getMessage().getMessageId();
        } else {
            message_id = update.getMessage().getMessageId();
        }
        EditMessageText editMessage = new EditMessageText();
        editMessage.setText(sendMessage.getText());
        editMessage.setMessageId(toIntExact(message_id));
        if (update.hasCallbackQuery()) {
            editMessage.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
        } else {
            editMessage.setChatId(update.getMessage().getChatId().toString());
        }
        editMessage.enableHtml(true);
        if (sendMessage.getReplyMarkup() != null) {
          editMessage.setReplyMarkup((InlineKeyboardMarkup) sendMessage.getReplyMarkup());
        }
        return editMessage;
    }

    public void reply(Update update, String text) {
        SendMessage reply;
        if (update.hasCallbackQuery()) {
            reply = msgObject(update.getCallbackQuery().getMessage().getChatId(), text);
        } else {
            reply = msgObject(update.getMessage().getChatId(), text);
        }
        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);
        reply.setReplyMarkup(replyKeyboardRemove);
        botKeeper.getBot().executeMessage(reply);
    }

    public void reply(Update update, String text, ReplyKeyboardRemove markup) {
        SendMessage reply;
        if (update.hasCallbackQuery()) {
            reply = msgObject(update.getCallbackQuery().getMessage().getChatId(), text);
        } else {
            reply = msgObject(update.getMessage().getChatId(), text);
        }
        reply.setReplyMarkup(markup);
        botKeeper.getBot().executeMessage(reply);
    }

    public void reply(SendMessage sendMessage) {
        botKeeper.getBot().executeMessage(sendMessage);
    }

    public void deleteMessage(Long chatId, Integer messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setMessageId(messageId);
        deleteMessage.setChatId(chatId);
        botKeeper.getBot().executeMessage(deleteMessage);
    }
}
