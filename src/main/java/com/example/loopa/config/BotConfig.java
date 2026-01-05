//package com.example.loopa.config;
//
//import com.example.loopa.service.bot.BotService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.telegram.telegrambots.meta.TelegramBotsApi;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
//
//@Configuration
//public class BotConfig {
//
//    @Bean
//    public TelegramBotsApi telegramBotsApi(BotService botService) throws TelegramApiException {
//        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
//        api.registerBot(botService);
//        return api;
//    }
//}