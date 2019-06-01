package no.fint;

import com.github.seratch.jslack.app_backend.slash_commands.payload.SlashCommandPayloadParser;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Configuration
public class ApplicationConfig {

    @Value("${fint.apistatus.url:http://localhost:8081/api/healthcheck/}")
    private String apiStatusUrl;

    @Getter
    @Value("${fint.apistatus.environments:api,beta,play-with-fint}")
    private List<String> environments;


    @Bean
    public WebClient healthWebClient() {
        return WebClient.builder()
                .baseUrl(apiStatusUrl)
                .build();
    }

    @Bean
    public SlashCommandPayloadParser slashCommandPayloadParser() {
        return new SlashCommandPayloadParser();
    }


}
