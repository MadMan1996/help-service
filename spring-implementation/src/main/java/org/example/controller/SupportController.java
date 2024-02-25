package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.example.dto.SupportPhraseDto;
import org.example.entity.SupportPhrase;
import org.example.service.SupportService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/help-service/v1")
@RequiredArgsConstructor
public class SupportController {
    private final SupportService supportService;
    private final ObjectMapper objectMapper;

    @GetMapping("/support")
    public SupportPhraseDto getSupport() {
       return objectMapper.convertValue(supportService.getSupportPhrase(), SupportPhraseDto.class);
    }

    @PostMapping("/support")
    public void addSupport(@RequestBody SupportPhraseDto phraseDto) {
        supportService.addSupportPhrase(objectMapper.convertValue(phraseDto, SupportPhrase.class));
    }

}
