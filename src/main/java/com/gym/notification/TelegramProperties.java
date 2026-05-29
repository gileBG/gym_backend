package com.gym.notification;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "telegram")
public class TelegramProperties {

    private boolean enabled = false;
    private String botToken;
    private String chatId;
}