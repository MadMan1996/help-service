package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.Publisher;
import org.example.dto.SupportPhraseDto;
import org.example.service.SupportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/help-service/v1")
public class SupportController {
    private final SupportService supportService;
    private final ObjectMapper objectMapper;
    private final Publisher<SupportPhraseDto> supportPhrasePublisher;

    public SupportController(SupportService supportService, ObjectMapper objectMapper, Publisher<SupportPhraseDto> publisher) {
        this.supportService = supportService;
        this.objectMapper = objectMapper;
        this.supportPhrasePublisher = publisher;
    }

    @GetMapping("/support")
    public ResponseEntity<SupportPhraseDto> getSupport() {
        SupportPhraseDto supportPhraseDto = objectMapper.convertValue(supportService.getSupportPhrase(), SupportPhraseDto.class);
        return ResponseEntity.status(200).body(supportPhraseDto);
    }

    @PostMapping("/support")
    public ResponseEntity<Void> addSupport(@RequestBody SupportPhraseDto phraseDto) {
        supportService.addSupportPhrase(phraseDto);
        return ResponseEntity.status(200).build();
    }

}
