package com.bui.projects.repository;

import com.bui.projects.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {

    Optional<AccountEntity> findByChatId(Long chatId);

}
