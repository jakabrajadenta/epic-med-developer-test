package com.epicmed.developer.assessment.client;

import com.epicmed.developer.assessment.dto.DummyDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Log4j2
@Service
public class UserRestClient {

    @Value("${user.service.base.url}")
    private String userBaseUrl;
    @Value("${user.data.path}")
    private String userDataPath;

    private final WebClient webClient;

    public UserRestClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public DummyDto getUsers()  {
        var uri = UriComponentsBuilder.fromUriString(userBaseUrl + userDataPath).toUriString();
        log.info("WebClient Target GET {}", uri);
        var responseEntity = webClient.get().uri(uri)
                .exchangeToMono(clientResponse -> clientResponse.toEntity(DummyDto.class))
                .block();
        assert responseEntity != null;
        return responseEntity.getBody();
    }
}
