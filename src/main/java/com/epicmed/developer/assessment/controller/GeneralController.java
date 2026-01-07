package com.epicmed.developer.assessment.controller;

import com.epicmed.developer.assessment.dto.GeneralDto;
import com.epicmed.developer.assessment.service.GeneralService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/general")
public class GeneralController {

    private GeneralService generalService;

    @PostMapping("/echo")
    public ResponseEntity<GeneralDto> echoMessage(@RequestBody GeneralDto generalDto) {
        log.info("GeneralController::echoMessage started");
        return ResponseEntity.ok(generalService.echoMessage(generalDto));
    }
}
