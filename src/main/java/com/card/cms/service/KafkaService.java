package com.card.cms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;

@Service
public class KafkaService {

    private static  final Logger LOGGER = LoggerFactory.getLogger(KafkaService.class);

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    public void send(String key, String topicName, Object data) {
        LOGGER.info("SENDING TO KAFKA WITH KEY:{}, TOPIC:{}", key, topicName);
        try {
            ListenableFuture<SendResult<Object, Object>> future = kafkaTemplate.send(topicName, key, data);
            SuccessCallback<SendResult<Object, Object>> successCallback = sendResult -> {
                LOGGER.info("Sent payload='" + data + "' with key='" + key
                        + "' to topic-partition@offset='" + sendResult.getRecordMetadata().toString() + "'");
            };

            FailureCallback failureCallback = throwable -> {
                LOGGER.error("Sending payload='" + data + "' to topic='" + topicName + "' with key='" + key + "' failed!!!");
            };
            future.addCallback(successCallback, failureCallback);
        } catch (Throwable t) {
            LOGGER.error("ERROR SENDING KAFKA:",t);
        }
    }
}
