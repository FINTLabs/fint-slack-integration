package no.fint.slack.command.service;

import com.github.seratch.jslack.api.model.block.DividerBlock;
import com.github.seratch.jslack.api.model.block.LayoutBlock;
import com.github.seratch.jslack.api.model.block.SectionBlock;
import com.github.seratch.jslack.api.model.block.composition.MarkdownTextObject;
import com.github.seratch.jslack.api.model.block.composition.PlainTextObject;
import com.github.seratch.jslack.app_backend.slash_commands.response.SlashCommandResponse;
import no.fint.ApplicationConfig;
import no.fint.slack.command.Emoji;
import no.fint.slack.command.command.SlashCommandResponseType;
import no.fint.slack.command.model.HealthCheckResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static no.fint.ApplicationConfig.TargetService.ServiceTypes.HEALTH;
import static no.fint.ApplicationConfig.TargetService;

@Service
public class HealthService {

    @Autowired
    @TargetService(HEALTH)
    private WebClient healthWebClient;

    @Autowired
    private ApplicationConfig config;

    public SlashCommandResponse getHealthByEnvironment(String environment) {
        return SlashCommandResponse
                .builder()
                .text(String.format("*Health status for* `%s`", environment))
                .blocks(getLayoutBlocks(environment))
                .responseType(SlashCommandResponseType.EPHEMERAL)
                .build();
    }

    public SlashCommandResponse getHealth() {
        return SlashCommandResponse.builder()
               .text(String.format("*Health status for alle enviornments* %s", Emoji.HEART))
               .blocks(config.getEnvironments().stream().map(this::getLayoutBlocks).flatMap(Collection::stream).collect(Collectors.toList()))
               .responseType(SlashCommandResponseType.EPHEMERAL)
               .build();
    }

    private List<LayoutBlock> getLayoutBlocks(String environment) {
        Flux<HealthCheckResponse> healthCheckResponseFlux = healthWebClient.get().uri(environment).retrieve().bodyToFlux(HealthCheckResponse.class);

        List<HealthCheckResponse> healthCheckResponses = healthCheckResponseFlux.collectList().block(Duration.ofSeconds(15));

        List<LayoutBlock> blocks = new ArrayList<>();

        blocks.add(
                SectionBlock
                        .builder()
                        .text(MarkdownTextObject
                                .builder()
                                .text(String.format("*Health status for* `%s`", environment))
                                .build()
                        )
                        .build()

        );
        blocks.add(new DividerBlock());


        Objects.requireNonNull(healthCheckResponses).forEach(healthCheckResponse -> {
            blocks.add(
                    SectionBlock
                            .builder()
                            .text(PlainTextObject
                                    .builder()
                                    .emoji(true)
                                    .text(String.format("%s     %s", healthCheckResponse.isHealthy() ? Emoji.HEART : Emoji.BROKEN_HEART, healthCheckResponse.getProps().getName()))
                                    .build()
                            )
                            .build()
            );
            blocks.add(new DividerBlock());
        });
        return blocks;
    }


}
