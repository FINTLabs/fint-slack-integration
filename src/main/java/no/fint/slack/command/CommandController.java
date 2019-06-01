package no.fint.slack.command;

import com.github.seratch.jslack.app_backend.slash_commands.payload.SlashCommandPayload;
import com.github.seratch.jslack.app_backend.slash_commands.payload.SlashCommandPayloadParser;
import com.github.seratch.jslack.app_backend.slash_commands.response.SlashCommandResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/command")
public class CommandController {


    @Autowired
    private CommandExecuter commandExecuter;

    @Autowired
    private SlashCommandPayloadParser slashCommandPayloadParser;

    @PostMapping()
    public SlashCommandResponse command(@RequestBody String body) {

        SlashCommandPayload slashCommandPayload = slashCommandPayloadParser.parse(body);

        log.info("Running command: \"{} {}\"", slashCommandPayload.getCommand(), slashCommandPayload.getText());

        return commandExecuter.execute(slashCommandPayload.getText()).orElse(SlashCommandResponse.builder()
                .text("*This is not a valid command* :headwall:")
                .responseType(SlashCommandResponseType.EPHEMERAL)
                .build());
    }
}
