package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.config.property.KafkaSupportServiceProp;
import org.example.entity.SupportPhrase;
import org.example.repository.SupportPhraseRepository;
import org.example.service.SupportService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SupportServiceImpl implements SupportService {
    private final List<KafkaTemplate<String, Object>> kafkaTemplates;
    private final SupportPhraseRepository supportPhraseRepository;
    private final KafkaSupportServiceProp props;

    @Override
    public SupportPhrase getSupportPhrase(){
        List<Long> ids = supportPhraseRepository.getIds();
        Long randomEntityIndex = ids.stream().skip(new Random().nextInt(ids.size())).findFirst().orElseGet(()->1L);
        return supportPhraseRepository.findById(randomEntityIndex).orElseGet(()->new SupportPhrase("Default support phrase"));

    }
    @Override
    public void addSupportPhrase(SupportPhrase supportPhrase){
        SupportPhrase entity = supportPhraseRepository.save(supportPhrase);
        for(var producer : kafkaTemplates){
            producer.send(props.getTopic(), entity);
        }
    }


}
