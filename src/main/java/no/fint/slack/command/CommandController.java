package no.fint.slack.command;

import com.github.seratch.jslack.app_backend.slash_commands.payload.SlashCommandPayload;
import com.github.seratch.jslack.app_backend.slash_commands.payload.SlashCommandPayloadParser;
import com.github.seratch.jslack.app_backend.slash_commands.response.SlashCommandResponse;
import lombok.extern.slf4j.Slf4j;
import no.fint.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;
import java.nio.file.AccessDeniedException;

@Slf4j
@RestController
@RequestMapping("/command")
public class CommandController {


    @Autowired
    private CommandExecuter commandExecuter;

    @Autowired
    private SlashCommandPayloadParser slashCommandPayloadParser;

    @Autowired
    private ApplicationConfig config;

    @PostMapping()
    public ResponseEntity command(@RequestBody String body) {

        SlashCommandPayload slashCommandPayload = slashCommandPayloadParser.parse(body);

        log.info("Running command: \"{} {}\"", slashCommandPayload.getCommand(), slashCommandPayload.getText());

        if (config.getSlackToken().equals(slashCommandPayload.getToken())) {
            return ResponseEntity.ok(commandExecuter.execute(slashCommandPayload.getText()).orElse(SlashCommandResponse.builder()
                    .text("*This is not a valid command* :headwall:")
                    .responseType(SlashCommandResponseType.EPHEMERAL)
                    .build()));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();


    }
}
