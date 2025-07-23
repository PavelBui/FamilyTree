package com.bui.projects.telegram;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class BotKeeper {

    private Bot telegramBot;

    public Bot getBot() {
        return telegramBot;
    }
}
