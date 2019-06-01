package no.fint.slack.command.service;

import com.github.seratch.jslack.api.model.block.LayoutBlock;
import com.github.seratch.jslack.api.model.block.SectionBlock;
import com.github.seratch.jslack.api.model.block.composition.MarkdownTextObject;
import com.github.seratch.jslack.app_backend.slash_commands.response.SlashCommandResponse;
import no.fint.ApplicationConfig;
import no.fint.slack.command.command.SlashCommandResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class EnvironmentService {


    @Autowired
    private ApplicationConfig config;

    public SlashCommandResponse getEnvironments() {

        return SlashCommandResponse.builder()
                .text("Environments")
                .blocks(config.getEnvironments().stream().map(this::getLayoutBlock).collect(Collectors.toList()))
                .responseType(SlashCommandResponseType.EPHEMERAL)
                .build();
    }

    private LayoutBlock getLayoutBlock(String env) {
        return SectionBlock
                .builder()
                .text(MarkdownTextObject
                        .builder()
                        .text(String.format("- `%s`", env))
                        .build())
                .build();
    }
}
