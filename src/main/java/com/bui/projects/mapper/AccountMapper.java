package com.bui.projects.mapper;

import com.bui.projects.dto.AccountDto;
import com.bui.projects.entity.AccountEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AccountMapper {


    public AccountEntity dtoToEntity(AccountDto accountDto) {
        AccountEntity accountEntity = new AccountEntity();
        return dtoToEntity(accountEntity, accountDto);
    }

    public AccountEntity dtoToEntity(AccountEntity accountEntity, AccountDto accountDto) {
        accountEntity.setChatId(accountDto.getChatId());
        accountEntity.setLastName(accountDto.getLastName());
        accountEntity.setFirstName(accountDto.getFirstName());
        accountEntity.setUserName(accountDto.getUserName());
        accountEntity.setState(accountDto.getState());
        return accountEntity;
    }

    public AccountDto entityToDto(AccountEntity accountEntity) {
        return AccountDto.builder()
                .chatId(accountEntity.getChatId())
                .lastName(accountEntity.getLastName())
                .firstName(accountEntity.getFirstName())
                .userName(accountEntity.getUserName())
                .state(accountEntity.getState())
                .build();
    }
}
