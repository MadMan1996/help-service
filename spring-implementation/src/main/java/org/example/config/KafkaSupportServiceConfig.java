package org.example.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.config.property.KafkaSupportServiceProp;
import org.example.entity.SupportPhrase;
import org.example.listener.KafkaSupportListener;
import org.example.repository.SupportPhraseRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@ConditionalOnProperty(prefix = "kafka.support-service", value = "enabled", havingValue = "true")
@EnableKafka
public class KafkaSupportServiceConfig {

    @Bean
    @ConfigurationProperties(prefix = "kafka.support-service")
    public KafkaSupportServiceProp kafkaSupportServiceProp() {
        return new KafkaSupportServiceProp();
    }

    @Bean
    public KafkaAdmin kafkaAdmin(KafkaSupportServiceProp props) {
        Map<String, java.lang.Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, props.getServer());
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic supportPhraseTopic(KafkaSupportServiceProp props) {
        return new NewTopic(props.getTopic(), 1, (short) 1);
    }

    @Bean
    public ProducerFactory<String, Object> commonProducerFactory() {
        Map<String, java.lang.Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaSupportServiceProp().getServer());
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }


    @Bean
    public KafkaTemplate<String, Object> connomKakfaTemplate(ProducerFactory<String, Object> supportProducerFactory) {
        return new KafkaTemplate<>(supportProducerFactory);
    }


    @Bean
    public ConsumerFactory<String, SupportPhrase> supportPhraseConsumerFactory() {
        Map<String, java.lang.Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaSupportServiceProp().getServer());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(SupportPhrase.class));

    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SupportPhrase> supportPhraseKafkaListenerContainerFactory(
            ConsumerFactory<String, SupportPhrase> supportPhraseConsumerFactory,
            DefaultErrorHandler supportErrorHandler
    ) {
        ConcurrentKafkaListenerContainerFactory<String, SupportPhrase> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(supportPhraseConsumerFactory);
        containerFactory.setCommonErrorHandler(supportErrorHandler);
        return containerFactory;
    }

    @Bean
    public KafkaSupportListener supportListener(SupportPhraseRepository supportPhraseRepository) {
        return new KafkaSupportListener(supportPhraseRepository);
    }

    @Bean
    public DefaultErrorHandler supportErrorHandler(
            KafkaTemplate<String, Object> connomKakfaTemplate, KafkaSupportServiceProp props
            )
    {
        BackOff fixedBackOff = new FixedBackOff(props.getBackoff().getInterval(), props.getBackoff().getMaxAttempts());
        return new DefaultErrorHandler((consumerRecord, exception) -> {
            connomKakfaTemplate.send(consumerRecord.topic() + props.getDlq(),
                    String.format("Processing record %s failed with exception %s",  consumerRecord.value().toString(), exception.getMessage()));
        }, fixedBackOff);
    }

}
