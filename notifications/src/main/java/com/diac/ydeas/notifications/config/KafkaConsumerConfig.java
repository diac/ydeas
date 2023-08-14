package com.diac.ydeas.notifications.config;

import com.diac.ydeas.domain.dto.IdeaReviewNotificationDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурация Kafka-консьюмера
 */
@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    /**
     * Доверенные пакеты десериализации
     */
    private static final String DESERIALIZER_TRUSTED_PACKAGES = "com.diac.ydeas.domain.dto";

    /**
     * Адрес Kafka-сервера
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaServer;

    /**
     * ID группы консьюмеров
     */
    @Value("${spring.kafka.consumer.group-id}")
    private String kafkaGroupId;

    /**
     * Бин, определяющий конфигурацию консьюмера
     *
     * @return Карта с конфигурацией консьюмера
     */
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
        return props;
    }

    /**
     * Бин-фабрика Kafka-консьюмера для объектов IdeaReviewNotificationDto
     *
     * @return Фабрика Kafka-консьюмера для объектов IdeaReviewNotificationDto
     */
    @Bean
    public ConsumerFactory<Integer, IdeaReviewNotificationDto> ideaReviewConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new IntegerDeserializer(),
                valueDeserializer()
        );
    }

    /**
     * Бин-фабрика контейнера Kafka-консьюмера для объектов IdeaReviewNotificationDto
     *
     * @return Фабрика контейнера Kafka-консьюмера для объектов IdeaReviewNotificationDto
     */
    @Bean
    public KafkaListenerContainerFactory<?> ideaReviewKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, IdeaReviewNotificationDto> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(ideaReviewConsumerFactory());
        return factory;
    }

    /**
     * Получить десериализатор для объектов IdeaReviewNotificationDto
     *
     * @return Десериализатор для объектов IdeaReviewNotificationDto
     */
    private Deserializer<IdeaReviewNotificationDto> valueDeserializer() {
        JsonDeserializer<IdeaReviewNotificationDto> valueDeserializer = new JsonDeserializer<>();
        valueDeserializer.addTrustedPackages(DESERIALIZER_TRUSTED_PACKAGES);
        return valueDeserializer;
    }
}