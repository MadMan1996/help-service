package org.example.config.property;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaSupportServiceProp {
    private String server="http://localhost:9092";
    private Boolean enabled = false;
    private String topic= "support_phrase";
    private String dlq="_dlq";
    private Backoff backoff = new Backoff();

    @Setter
    @Getter
    public static class Backoff{
        private Long interval = 1L;
        private Long maxAttempts = 1L;
    }
}


