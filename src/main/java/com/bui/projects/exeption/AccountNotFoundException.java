package com.bui.projects.exeption;

public class AccountNotFoundException extends  RuntimeException{

    public AccountNotFoundException(Long chatId) {
        super("Account with chat id: " + chatId + " not found");
    }
}
