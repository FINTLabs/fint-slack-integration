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

import java.util.*;


@Service
public class ClientService {

    @Autowired
    private WebClientClients clientsWebClient;

    @Autowired
    private ApplicationConfig config;

    public SlashCommandResponse getClients(Command command) {
        return SlashCommandResponse.builder()
                .text("Clients")
                .blocks(getClientListLayoutBlock(command.getParameters().get(0), command.getParameters().get(1)))
                .responseType(SlashCommandResponseType.EPHEMERAL)
                .build();
    }

    public SlashCommandResponse getClientByAsset(Command command) {
        return SlashCommandResponse.builder()
                .text("Clients")
                .blocks(getClientByAssetLayoutBlock(command.getParameters().get(0), command.getParameters().get(1), command.getParameters().get(2)))
                .responseType(SlashCommandResponseType.EPHEMERAL)
                .build();
    }

    private List<SseClients> getClientsFromComponent(String environment, String componentUri) {
        return Arrays.asList(Objects.requireNonNull(clientsWebClient.get(
                String.format(
                        config.getSseClientsUriTemplate(),
                        environment,
                        componentUri
                ))
                .bodyToMono(SseClients[].class).block()));

    }

    private List<LayoutBlock> getClientByAssetLayoutBlock(String environment, String componentUri, String asset) {
        Optional<SseClients> sseClients = getClientsFromComponent(environment, componentUri)
                .stream()
                .filter(c -> c.getOrgId().equals(asset))
                .findFirst();
        List<LayoutBlock> blocks = new ArrayList<>();
        sseClients.ifPresent(clients -> {
            blocks.add(SectionBlock.builder().text(MarkdownTextObject
                    .builder()
                    .text(String.format("*Registered clients for* `%s` *:*", clients.getOrgId()))
                    .build())
                    .build()
            );
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
        });

        return blocks;

    }

    private List<LayoutBlock> getClientListLayoutBlock(String environment, String componentUri) {
        List<SseClients> sseClients = getClientsFromComponent(environment, componentUri);
        List<LayoutBlock> blocks = new ArrayList<>();

        blocks.add(
                SectionBlock
                        .builder()
                        .text(MarkdownTextObject
                                .builder()
                                .text(String.format("*Listing clients for* `https://%s.felleskomponent.no/%s`:",
                                        environment,
                                        componentUri)
                                )
                                .build()
                        )
                        .build()

        );
        blocks.add(new DividerBlock());

        List<String> clientList = new ArrayList<>();
        sseClients.forEach(clients -> clientList.add(
                String.format("- `%s` clients for `%s`",
                        clients.getClients().size(),
                        clients.getOrgId())
                )
        );

        blocks.add(
                SectionBlock
                        .builder()
                        .text(MarkdownTextObject
                                .builder()
                                .text(String.join("\n", clientList))
                                .build()
                        )
                        .build()
        );
        blocks.add(new DividerBlock());

        return blocks;

    }

}
