package com.bui.projects.telegram.service;

import com.bui.projects.telegram.config.BotRunner;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupService {

    private final BotRunner botRunner;

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        botRunner.run();
    }
}
