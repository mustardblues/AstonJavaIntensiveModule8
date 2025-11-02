package edu.aston.userservice.producer;

import edu.aston.userservice.dto.UserEventDTO;

import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

@Service
public class UserEventProducer {
    private final KafkaTemplate<String, UserEventDTO> kafkaTemplate;

    public UserEventProducer(KafkaTemplate<String, UserEventDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(final String action, final String email) {
        kafkaTemplate.send("user-events-topic", new UserEventDTO(action, email));
    }
}
