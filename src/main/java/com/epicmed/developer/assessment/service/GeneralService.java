package com.epicmed.developer.assessment.service;

import com.epicmed.developer.assessment.dto.GeneralDto;
import com.epicmed.developer.assessment.exception.GeneralException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.epicmed.developer.assessment.util.constant.GeneralConstant.RESP_CODE_SUCCESS;
import static com.epicmed.developer.assessment.util.constant.GeneralConstant.RESP_MESSAGE_SUCCESS;

@Log4j2
@Service
public class GeneralService {

    public GeneralDto echoMessage(GeneralDto generalDto) {
        log.info("GeneralService::echoMessage started");
        generalDto.setResponseCode(RESP_CODE_SUCCESS);
        generalDto.setResponseMessage(RESP_MESSAGE_SUCCESS);
        if (Objects.equals(generalDto.getKey(),"invalid")) throw new GeneralException("Validate key, error key field at " + LocalDateTime.now());
        if (Objects.equals(generalDto.getKey(),"error")) throw new GeneralException("Do not honor, error key field at " + LocalDateTime.now());
        return generalDto;
    }
}
