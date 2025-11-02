package edu.aston.notificationservice.controller;

import edu.aston.notificationservice.dto.UserEventDTO;
import edu.aston.notificationservice.service.NotificationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/application/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(@RequestBody UserEventDTO userEventDTO) {
        notificationService.handleEvent(userEventDTO.getOperation(), userEventDTO.getEmail());
        return ResponseEntity.ok().build();
    }
}
