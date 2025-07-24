package com.bui.projects.telegram.session;

import com.bui.projects.dto.AccountDto;
import com.bui.projects.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

import static com.bui.projects.telegram.config.State.START;

@Component
@RequiredArgsConstructor
public class TelegramRequestContext {

    private static Sessions sessions;

    private final AccountService accountService;

    public static SessionUser requestUser(Long chatId) {
        return sessions.getByChatId(chatId);
    }

    public void checkUser(Update update) {
        Long chatId = getChatId(update);
        User user = getUser(update);
        if (!sessions.existsByChatId(chatId)) {
            if (accountService.isPresent(chatId)) {
                AccountDto account = accountService.getAccount(chatId);
                account.setUserName(getUsername(user));
                account.setFirstName(user.getFirstName());
                account.setLastName(user.getLastName());
                sessions.setSession(account);
            } else {
                SessionUser sessionUser =
                        SessionUser.builder()
                                .chatId(chatId)
                                .state(START)
                                .firstname(user.getFirstName())
                                .lastname(user.getLastName())
                                .username(getUsername(user))
                                .originUserName(user.getUserName())
                                .build();
                sessions.setSession(chatId, Optional.of(sessionUser));
            }
        }
        SessionUser sessionUser = sessions.getByChatId(chatId);
        AccountDto account =
                AccountDto.builder()
                        .chatId(chatId)
                        .state(sessionUser.getState())
                        .userName(sessionUser.getUsername())
                        .lastName(sessionUser.getLastname())
                        .firstName(sessionUser.getFirstname())
                        .build();
        if (!accountService.isPresent(chatId)) {
            accountService.createAccount(account);
        } else {
            accountService.updateAccount(account);
        }
    }

    private Long getChatId(Update update) {
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId();
        } else {
            return update.getMessage().getChatId();
        }
    }

    private User getUser(Update update) {
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom();
        } else {
            return update.getMessage().getFrom();
        }
    }

    private String getUsername(User user) {
        return user.getUserName() != null ? "https://t.me/" + user.getUserName() : null;
    }

    @Autowired
    public void setSessions(Sessions sessions) {
        TelegramRequestContext.sessions = sessions;
    }
}
