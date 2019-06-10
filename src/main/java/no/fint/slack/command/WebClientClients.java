package no.fint.slack.command;

import lombok.extern.slf4j.Slf4j;
import no.fint.oauth.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import static no.fint.ApplicationConfig.TargetService;
import static no.fint.ApplicationConfig.TargetService.ServiceTypes.CLIENT;


@Slf4j
@Component
public class WebClientClients {

    @Autowired
    @TargetService(CLIENT)
    private WebClient webClient;

    @Autowired
    private TokenService tokenService;

    public <T> ResponseSpec get(String uri) {
        WebClient.RequestHeadersSpec<?> request = webClient.get().uri(uri);
        return get(uri, request);
    }

    private <T> ResponseSpec get(String uri, WebClient.RequestHeadersSpec<?> request) {
        String token = tokenService.getBearerToken(uri);
        log.debug("Token: {}", token);
        if (token != null) {
            request.header(HttpHeaders.AUTHORIZATION, token);
        }
        return request.retrieve();
    }


}