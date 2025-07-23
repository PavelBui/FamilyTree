package com.bui.projects.service.impl;

import com.bui.projects.dto.AccountDto;
import com.bui.projects.entity.AccountEntity;
import com.bui.projects.exeption.AccountNotFoundException;
import com.bui.projects.mapper.AccountMapper;
import com.bui.projects.repository.AccountRepository;
import com.bui.projects.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private AccountMapper accountMapper;
    private AccountRepository accountRepository;

    @Override
    @Transactional
    public void createAccount(AccountDto accountDto) {
        AccountEntity accountEntity = accountMapper.dtoToEntity(accountDto);
        accountEntity.setCreatedAt(LocalDateTime.now());
        accountRepository.save(accountEntity);
    }

    @Override
    @Transactional
    public void updateAccount(AccountDto accountDto) {
        Long chatId = accountDto.getChatId();
        AccountEntity accountEntity = accountRepository.findByChatId(chatId)
                .orElseThrow(() -> new AccountNotFoundException(chatId));
        AccountEntity updatedAccountEntity = accountMapper.dtoToEntity(accountEntity, accountDto);
        accountEntity.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(updatedAccountEntity);
    }

    @Override
    @Transactional
    public AccountDto getAccount(Long chatId) {
        return accountMapper.entityToDto(accountRepository.findByChatId(chatId)
                .orElseThrow(() -> new AccountNotFoundException((chatId))));
    }

    @Override
    @Transactional
    public boolean isPresent(Long chatId) {
        return accountRepository.findByChatId(chatId).isPresent();
    }
}
