package com.infrrd.controller;

import com.infrrd.dto.RegexMatchReqDTO;
import com.infrrd.dto.RegexMatchRespDTO;
import com.infrrd.service.RegexMatchService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class InfrrdRestController {

    @Autowired
    RegexMatchService regexMatchService;


    @PostMapping("/regex")
    @SneakyThrows
    public ResponseEntity<RegexMatchRespDTO> matchRegex(@RequestBody @Valid RegexMatchReqDTO regexMatchReqDTO){
        return ResponseEntity.ok(regexMatchService.matchInputTextAgainstRegexExp(regexMatchReqDTO.getRegex(),regexMatchReqDTO.getTextBody()));
    }




}
