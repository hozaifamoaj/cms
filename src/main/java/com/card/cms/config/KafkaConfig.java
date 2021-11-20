package com.card.cms.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Value("${kafka.partition.count}")
    private Integer partitionCount;

    @Value("${kafka.replication.factor}")
    private short replicationFactor;

    @Bean
    public NewTopic topicSendMail() {
        return new NewTopic("send_mail", partitionCount, replicationFactor);
    }

    @Bean
    public NewTopic topicRecharge() {
        return new NewTopic("recharge_balance", partitionCount, replicationFactor);
    }
}
