package com.bui.projects.service;

import com.bui.projects.dto.AccountDto;

public interface AccountService {

    void createAccount(AccountDto accountDto);

    void updateAccount(AccountDto accountDto);

    AccountDto getAccount(Long chatId);

    boolean isPresent(Long chatId);
}
