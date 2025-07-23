package com.bui.projects.telegram.session;

import com.bui.projects.dto.AccountDto;
import com.bui.projects.telegram.config.State;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.nonNull;

@Component
public class Sessions {

    public Map<Long, Optional<SessionUser>> sessions = new ConcurrentHashMap<>();

    public Optional<SessionUser> findByChatId(Long chatId) {
        return sessions.get(chatId);
    }

    public boolean existsByChatId(Long chatId) {
        Optional<SessionUser> sessionUser = sessions.get(chatId);
        return sessionUser != null && sessionUser.isPresent();
    }

    public SessionUser getByChatId(Long chatId) {
        Optional<SessionUser> sessionUser = sessions.get(chatId);
        return sessionUser.orElse(null);
    }

    public Boolean checkState(State state, Long chatId) {
        Optional<SessionUser> session = findByChatId(chatId);
        return session.map(sessionUser -> sessionUser.getState().equals(state)).orElse(false);
    }

    public void setSession(Long chatId, Optional<SessionUser> user) {
        sessions.put(chatId, user);
    }

    public void setSession(AccountDto account) {
        SessionUser sessionUser =
                SessionUser.builder()
                        .chatId(account.getChatId())
                        .state(account.getState())
                        .firstname(account.getFirstName())
                        .lastname(account.getLastName())
                        .username(account.getUserName())
                        .originUserName(getOriginUserName(account.getUserName()))
                        .build();
        setSession(account.getChatId(), Optional.of(sessionUser));
    }

    private String getOriginUserName(String username) {
        if (nonNull(username)) {
            return username.substring(username.lastIndexOf("/") + 1);
        } else {
            return null;
        }
    }

    public void setState(State state, Long chatId) {
        Optional<SessionUser> session = findByChatId(chatId);
        if (session.isPresent()) {
            session.get().setState(state);
            setSession(chatId, session);
        }
    }

}
