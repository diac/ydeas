package com.diac.ydeas.ideas.config;

import com.diac.ydeas.domain.dto.IdeaReviewNotificationDto;
import com.fasterxml.jackson.databind.JsonSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурация Kafka-продюсера
 */
@Configuration
public class KafkaProducerConfig {

    /**
     * Адрес Kafka-сервера
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaServer;

    /**
     * Бин, определяющий конфигурацию продюсера
     *
     * @return Карта с конфигурацией продюсера
     */
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    /**
     * Бин-фабрика Kafka-продюсера для объектов IdeaReviewNotificationDto
     * @return Фабрика Kafka-продюсера для объектов IdeaReviewNotificationDto
     */
    @Bean
    public ProducerFactory<Integer, IdeaReviewNotificationDto> ideaReviewProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     * Бин-шаблон Kafka-продюсера для отправки объектов IdeaReviewNotificationDto
     * @return Шаблон Kafka-продюсера для отправки объектов IdeaReviewNotificationDto
     */
    @Bean
    public KafkaTemplate<Integer, IdeaReviewNotificationDto> ideaReviewKafkaTemplate() {
        return new KafkaTemplate<>(ideaReviewProducerFactory());
    }
}