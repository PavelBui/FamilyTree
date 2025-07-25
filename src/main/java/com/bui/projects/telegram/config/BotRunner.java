package com.bui.projects.telegram.config;

import com.bui.projects.telegram.Bot;
import com.bui.projects.telegram.BotKeeper;
import com.bui.projects.telegram.handler.UpdateHandler;
import com.bui.projects.telegram.session.TelegramRequestContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.*;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BotRunner {

    private static final String BOT_WAS_NOT_CREATED_MESSAGE = "Telegram bots Api wasn't created";

    private final UpdateHandler updateHandler;
    private final TelegramRequestContext telegramRequestContext;
    private final BotKeeper botKeeper;
    private final TelegramBotProperties telegramBotProperties;

    public void run() {
        log.info("Starting Telegram Bot");
        try {
            var api = new TelegramBotsApi(DefaultBotSession.class);
            Bot bot = createBot(api);
            if (bot != null) {
                botKeeper.setTelegramBot(bot);
                SetMyCommands setMyCommands = new SetMyCommands();
                List<BotCommand> botCommandList = List.of(
                        new BotCommand("me", "It's me"),
                        new BotCommand("home", "Go home"),
                        new BotCommand("start", "Restart")
                );
                setMyCommands.setCommands(botCommandList);
                bot.execute(setMyCommands);
            }

        } catch (TelegramApiException e) {
            log.error(BOT_WAS_NOT_CREATED_MESSAGE, e);
        }
    }

    private Bot createBot(TelegramBotsApi api) {
        try {
            Bot bot = new Bot(updateHandler, telegramRequestContext, telegramBotProperties);
            api.registerBot(bot);
            log.info("Connected to telegram bot");
            return bot;
        } catch (TelegramApiException e) {
            log.error("Telegram familyTreeBot is not connected", e);
            return null;
        }
    }
}
