package no.fint.slack.command;

import com.github.seratch.jslack.app_backend.slash_commands.response.SlashCommandResponse;
import no.fint.slack.command.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CommandExecuter {

    @Autowired
    private HealthService healthService;

    public Optional<SlashCommandResponse> execute(String commandText) {


        Optional<Command> command = CommandParser.parse(commandText);

        if (command.isPresent()) {
            if (command.get().getClass().getName().equals(GetHealthCommand.class.getName())) {
                GetHealthCommand getHealthCommand = (GetHealthCommand) command.get();
                if (getHealthCommand.isAll()) {
                    return Optional.ofNullable(healthService.getHealth());
                }
                return Optional.ofNullable(healthService.getHealthByEnvironment(((GetHealthCommand) command.get()).getEnvironment()));
            }
        }
        return Optional.empty();
    }
}
