package org.example.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.annotation.Controller;
import org.example.controller.SupportController;
import org.example.dto.SupportPhraseDto;
import org.example.entity.SupportPhrase;
import org.example.service.SupportService;

import java.io.IOException;

@Controller
public class SupportControllerImpl implements SupportController {
    private final SupportService supportService;
    private final ObjectMapper objectMapper;

    public SupportControllerImpl(SupportService supportService, ObjectMapper objectMapper) {
        this.supportService = supportService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void getSupport(HttpServletRequest req, HttpServletResponse res) throws IOException {
        SupportPhraseDto supportPhraseDto = objectMapper.convertValue(supportService.getSupportPhrase(), SupportPhraseDto.class);
        res.getWriter().write(objectMapper.writeValueAsString(supportPhraseDto));
    }

    @Override
    public void addSupport(HttpServletRequest req, HttpServletResponse res) throws IOException {
        SupportPhraseDto newPhraseDto = objectMapper.readValue(req.getReader().lines().reduce("", String::concat), SupportPhraseDto.class);
        supportService.addSupportPhrase(objectMapper.convertValue(newPhraseDto, SupportPhrase.class));
    }

}
