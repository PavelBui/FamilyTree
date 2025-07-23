package com.bui.projects.dto;

import com.bui.projects.telegram.config.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    private Long chatId;
    private State state;
    private String firstName;
    private String lastName;
    private String userName;
}
