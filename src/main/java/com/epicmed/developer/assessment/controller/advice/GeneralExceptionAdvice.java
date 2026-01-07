package com.epicmed.developer.assessment.controller.advice;

import com.epicmed.developer.assessment.dto.error.DefaultErrorAttribute;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalDateTime;
import java.util.Objects;


@Log4j2
@RestControllerAdvice
public class GeneralExceptionAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({Exception.class})
    public DefaultErrorAttribute exception(Exception ex, HttpServletRequest request) {
        log.error("GeneralExceptionAdvice::exception message {}", ex.getMessage());
        log.error("GeneralExceptionAdvice::exception Error", ex);
        var httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
        var httpMessage = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
        if (Objects.nonNull(ex.getMessage()) && ex.getMessage().contains("Validate")) {
            httpStatus = HttpStatus.BAD_REQUEST.value();
            httpMessage = HttpStatus.BAD_REQUEST.getReasonPhrase();
        }
        return DefaultErrorAttribute.builder()
                .status(httpStatus)
                .error(httpMessage)
                .path(request.getRequestURI())
                .message(ex.getLocalizedMessage())
                .timestamp(LocalDateTime.now().toString())
                .build();
    }
}
