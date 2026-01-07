package com.epicmed.developer.assessment.controller;

import com.epicmed.developer.assessment.dto.PageResponseDto;
import com.epicmed.developer.assessment.dto.UserRequestDto;
import com.epicmed.developer.assessment.dto.UserResponseDto;
import com.epicmed.developer.assessment.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {

    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<PageResponseDto<UserResponseDto>> getUsers(UserRequestDto userRequestDto) {
        log.info("GeneralController::echoMessage started");
        return ResponseEntity.ok(userService.getUsers(userRequestDto));
    }
}
