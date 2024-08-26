package com.ps.healthcare.service;

import org.springframework.kafka.annotation.KafkaListener;

public class KafkaConsumerService {

    @KafkaListener(topics = "patient-record-topic", groupId = "patient-record-group")
    public void listen(String message) {
        // Process the message
        System.out.println("Received message: " + message);
        // Add your processing logic here
    }


}
