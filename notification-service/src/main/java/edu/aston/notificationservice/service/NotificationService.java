package edu.aston.notificationservice.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final EmailService emailService;

    public NotificationService(final EmailService emailService) {
        this.emailService = emailService;
    }

    public void handleEvent(final String operation, final String email) {
        if("CREATE".equals(operation)) {
            emailService.sendMessageToEmail("create", email);
        }
        else if("DELETE".equals(operation)) {
            emailService.sendMessageToEmail("delete", email);
        }
    }
}
