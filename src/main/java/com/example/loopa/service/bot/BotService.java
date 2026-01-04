package com.example.loopa.service.bot;

import com.example.loopa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class BotService extends TelegramLongPollingBot {

    private final UserRepository userRepository;

    @Value("${bot.username}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() { return botUsername; }

    @Override
    public String getBotToken() { return botToken; }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String name = update.getMessage().getFrom().getFirstName();

            if (text.equals("/start")) {
                sendWelcomeMessage(chatId, name);
            }
        }
    }

    private void sendWelcomeMessage(Long chatId, String name) {
        String welcomeText = String.format(
                "Assalomu alaykum, <b>%s</b>! ✨\n\nLoopa platformasiga xush kelibsiz. " +
                        "Bu yerda siz sifatli mahsulotlarni topishingiz va o'z savdongizni boshlashingiz mumkin.\n\n" +
                        "Hamjamiyatimizga qo'shiling:", name);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton btn = new InlineKeyboardButton();
        btn.setText("Loopa Hamjamiyati 🌐");
        btn.setUrl("https://t.me/loopamarketuz");
        markup.setKeyboard(Collections.singletonList(Collections.singletonList(btn)));

        sendMessageWithKeyboard(chatId, welcomeText, markup);
    }

    public void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        message.setParseMode("HTML");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.err.println("Xabar yuborishda xatolik: " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 10 * * *")
    public void notifyInactiveUsers() {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);

        userRepository.findAllByLastLoginBefore(threeDaysAgo).forEach(user -> {
            String displayName = (user.getTgUsername() != null) ? "@" + user.getTgUsername() : "foydalanuvchi";
            String message = String.format(
                    "Sizni sog'indik, <b>%s</b>! 😊\n\n" +
                            "Loopa'da yangi mahsulotlar va qiziqarli e'lonlar paydo bo'ldi. " +
                            "Ularni o'tkazib yubormaslik uchun ilovaga kiring!", displayName);

            sendMessage(Long.parseLong(user.getChatId()), message);
        });
    }

    public void broadcastMessage(String text) {
        userRepository.findAll().forEach(user -> {
            try {
                sendMessage(Long.parseLong(user.getChatId()), text);
            } catch (Exception e) {
                System.err.println("Xatolik: " + user.getChatId());
            }
        });
    }

    private void sendMessageWithKeyboard(Long chatId, String text, InlineKeyboardMarkup markup) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        message.setReplyMarkup(markup);
        message.setParseMode("HTML");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
