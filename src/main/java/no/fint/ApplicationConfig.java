package no.fint;

import com.github.seratch.jslack.app_backend.slash_commands.payload.SlashCommandPayloadParser;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import static no.fint.ApplicationConfig.TargetService.ServiceTypes.CLIENT;
import static no.fint.ApplicationConfig.TargetService.ServiceTypes.HEALTH;

@Configuration
public class ApplicationConfig {

    @Value("${fint.apistatus.url:http://localhost:8081/api/healthcheck/}")
    private String apiStatusUrl;

    @Getter
    @Value("${fint.environments:api,beta,play-with-fint}")
    private List<String> environments;

    @Getter
    @Value("${fint.slack.token}")
    private String slackToken;

    @Getter
    @Value("${fint.sse-clients.uri-template:https://%s.felleskomponent.no/%s/provider/sse/clients}")
    private String sseClientsUriTemplate;


    @Bean
    @TargetService(HEALTH)
    public WebClient healthWebClient() {
        return WebClient.builder()
                .baseUrl(apiStatusUrl)
                .build();
    }

    @Bean
    @TargetService(CLIENT)
    public WebClient clientsWebClient() {
        return WebClient.builder()
                .build();
    }

    @Bean
    public SlashCommandPayloadParser slashCommandPayloadParser() {
        return new SlashCommandPayloadParser();
    }


    @Target({ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Qualifier
    public @interface TargetService {

        ServiceTypes value();

        enum ServiceTypes {
            HEALTH, CLIENT
        }
    }
}
