package no.fint.slack.command.service;

import com.github.seratch.jslack.api.model.block.DividerBlock;
import com.github.seratch.jslack.api.model.block.LayoutBlock;
import com.github.seratch.jslack.api.model.block.SectionBlock;
import com.github.seratch.jslack.api.model.block.composition.MarkdownTextObject;
import com.github.seratch.jslack.app_backend.slash_commands.response.SlashCommandResponse;
import no.fint.ApplicationConfig;
import no.fint.slack.command.WebClientClients;
import no.fint.slack.command.command.Command;
import no.fint.slack.command.command.SlashCommandResponseType;
import no.fint.slack.command.model.SseClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Service
public class ClientService {

    @Autowired
    private WebClientClients clientsWebClient;

    @Autowired
    private ApplicationConfig config;

    public SlashCommandResponse getClients(Command command) {
        return SlashCommandResponse.builder()
                .text("Clients")
                .blocks(getLayoutBlocks(command.getParameters().get(0), command.getParameters().get(1)))
                .responseType(SlashCommandResponseType.EPHEMERAL)
                .build();
    }

    private List<LayoutBlock> getLayoutBlocks(String environment, String componentUri) {
        List<SseClients> sseClients = Arrays.asList(Objects.requireNonNull(clientsWebClient.get(
                String.format(
                        config.getSseClientsUriTemplate(),
                        environment,
                        componentUri
                ))
                .bodyToMono(SseClients[].class).block()));

        List<LayoutBlock> blocks = new ArrayList<>();


        blocks.add(
                SectionBlock
                        .builder()
                        .text(MarkdownTextObject
                                .builder()
                                .text(String.format("*Listing clients for* `https://%s.felleskomponent.no/%s`:", environment, componentUri))
                                .build()
                        )
                        .build()

        );


        sseClients.forEach(clients -> {
            blocks.add(
                    SectionBlock
                            .builder()
                            .text(MarkdownTextObject
                                    .builder()
                                    .text(String.format("*Clients for* `%s` *(%s)*:", clients.getOrgId(), clients.getClients().size()))
                                    .build()
                            )
                            .build()
            );
            /*
            blocks.add(new DividerBlock());
            clients.getClients().forEach(client -> {
                blocks.add(SectionBlock
                        .builder()
                        .fields(Arrays.asList(
                                MarkdownTextObject
                                        .builder()
                                        .text(String.format("*Registered*:\n%s", client.getRegistered()))
                                        .build(),
                                MarkdownTextObject
                                        .builder()
                                        .text(String.format("*Id*:\n%s", client.getId()))
                                        .build(),
                                MarkdownTextObject
                                        .builder()
                                        .text(String.format("*Client*:\n%s", client.getClient()))
                                        .build(),
                                MarkdownTextObject
                                        .builder()
                                        .text(String.format("*Events*:\n%s", client.getEvents()))
                                        .build()
                        ))
                        .build()
                );
                blocks.add(new DividerBlock());

            });
             */
        });

        return blocks;

    }

}
