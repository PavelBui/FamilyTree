package com.bui.projects.telegram.session;

import com.bui.projects.telegram.util.enums.State;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class SessionUser {
    private Long chatId;
    private State state;
    private String firstname;
    private String lastname;
    private String username;
    private String originUserName;
    private Boolean isRegistered;
}
