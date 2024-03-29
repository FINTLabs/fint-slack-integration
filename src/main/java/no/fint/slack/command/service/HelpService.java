package no.fint.slack.command.service;

import com.github.seratch.jslack.api.model.block.SectionBlock;
import com.github.seratch.jslack.api.model.block.composition.MarkdownTextObject;
import com.github.seratch.jslack.app_backend.slash_commands.response.SlashCommandResponse;
import no.fint.slack.command.Emoji;
import no.fint.slack.command.command.SlashCommandResponseType;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class HelpService {

    public SlashCommandResponse getHelp() {

        return SlashCommandResponse.builder()
                .text(String.format("*Help* %s", Emoji.HEART))
                .blocks(Arrays.asList(
                        SectionBlock
                                .builder()
                                .text(MarkdownTextObject
                                        .builder()
                                        .text("The slash command `/fint` shows status on :fint: services:")
                                        .build()
                                )
                                .build(),
                        SectionBlock
                                .builder()
                                .text(MarkdownTextObject
                                        .builder()
                                        .text("```Commands:\n\t" +
                                                "get\n\t" +
                                                "help\n\n" +
                                                "Get command:\n\t" +
                                                "health [environment]                         Shows health status\n\t" +
                                                "envs                                         Lists environments\n\t" +
                                                "clients environment componentUri [assetId]   Lists registered clients```")
                                        .build()
                                )
                                .build()

                        )
                )
                .responseType(SlashCommandResponseType.EPHEMERAL)
                .build();
    }
}
