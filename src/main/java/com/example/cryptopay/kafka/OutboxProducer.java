package com.example.cryptopay.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OutboxProducer {
    private final KafkaTemplate<String,String> kafkaTemplate;
    public OutboxProducer(KafkaTemplate<String,String> kafkaTemplate) { this.kafkaTemplate = kafkaTemplate; }
    public void publish(String topic, String key, String payload) {
        kafkaTemplate.send(topic, key, payload);
    }
}
