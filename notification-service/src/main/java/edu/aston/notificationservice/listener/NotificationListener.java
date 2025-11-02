package edu.aston.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.aston.notificationservice.dto.UserEventDTO;
import edu.aston.notificationservice.service.NotificationService;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {
    private final NotificationService notificationService;

    public NotificationListener(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "user-events-topic", groupId = "notification-group")
    public void listen(final UserEventDTO userEventDTO) {
        notificationService.handleEvent(userEventDTO.getOperation(), userEventDTO.getEmail());
    }
}
