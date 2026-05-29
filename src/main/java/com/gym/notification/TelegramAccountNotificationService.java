package com.gym.notification;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TelegramAccountNotificationService implements AccountNotificationService {

    private static final Logger log = LoggerFactory.getLogger(TelegramAccountNotificationService.class);

    private final TelegramProperties telegramProperties;
    private final RestClient restClient = RestClient.create();

    @Override
    public void sendNewAccountNotification(String fullName, String email, String accountType) {
        if (!telegramProperties.isEnabled()) {
            return;
        }

        if (isBlank(telegramProperties.getBotToken()) || isBlank(telegramProperties.getChatId())) {
            log.warn("Telegram notifications enabled, but bot token or chat id is missing.");
            return;
        }

        String text = String.format(
                "Novi nalog napravljen.%nIme i prezime: %s%nEmail: %s%nRole naloga: %s",
                fullName,
                email,
                accountType
        );

        try {
            restClient.post()
                    .uri("https://api.telegram.org/bot{token}/sendMessage", telegramProperties.getBotToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "chat_id", telegramProperties.getChatId(),
                            "text", text
                    ))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception ex) {
            log.warn("Telegram notification failed for email {}", email, ex);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}