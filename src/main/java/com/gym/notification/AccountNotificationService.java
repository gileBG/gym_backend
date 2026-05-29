package com.gym.notification;

public interface AccountNotificationService {

    void sendNewAccountNotification(String fullName, String email, String accountType);
}