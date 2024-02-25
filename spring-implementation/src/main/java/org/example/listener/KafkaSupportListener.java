package org.example.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.SupportPhrase;
import org.example.repository.SupportPhraseRepository;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor
@EnableKafka
@Slf4j
public class KafkaSupportListener {
    private final SupportPhraseRepository supportPhraseRepository;

    @KafkaListener(topics = "${kafka.support-service.topic}", containerFactory = "supportPhraseKafkaListenerContainerFactory")
    public void syncInMemoryDb(SupportPhrase supportPhrase) {
        if (supportPhraseRepository.findById(supportPhrase.getId()).isEmpty()) {
            supportPhraseRepository.save(supportPhrase);
        } else {
            log.warn("Support phrase with id {} is already exists", supportPhrase.getId());
        }
    }
}
