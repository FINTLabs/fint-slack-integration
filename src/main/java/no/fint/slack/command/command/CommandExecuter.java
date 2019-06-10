package no.fint.slack.command.command;

import com.github.seratch.jslack.app_backend.slash_commands.response.SlashCommandResponse;
import no.fint.slack.command.service.ClientService;
import no.fint.slack.command.service.EnvironmentService;
import no.fint.slack.command.service.HealthService;
import no.fint.slack.command.service.HelpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CommandExecuter {

    @Autowired
    private HealthService healthService;

    @Autowired
    private HelpService helpService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private EnvironmentService environmentService;

    public Optional<SlashCommandResponse> execute(String commandText) {

        Optional<Command> command = CommandParser.parse(commandText);

        if (command.isPresent()) {
            switch (command.get().getCommand()) {
                case GET:
                    return onGetCommand(command.get());
                case HELP:
                    return onHelpCommand();

            }
        }

        return Optional.empty();
    }

    private Optional<SlashCommandResponse> onHelpCommand() {
        return Optional.ofNullable(helpService.getHelp());
    }

    private Optional<SlashCommandResponse> onGetCommand(Command command) {
        if (command.getSubCommand() != null) {
            if (command.getSubCommand().equals(Commands.Sub.HEALTH)) {
                return onHealthCommand(command);
            }
            if (command.getSubCommand().equals(Commands.Sub.ENVS)) {
                return Optional.ofNullable(environmentService.getEnvironments());
            }
            if (command.getSubCommand().equals(Commands.Sub.CLIENTS)) {
                return onClientsCommand(command);
            }
        }
        return Optional.empty();
    }

    // /fint get clients api administrasjon/personal [fintlabs.no]
    private Optional<SlashCommandResponse> onClientsCommand(Command command) {

        if (command.getParameters().size() == 2) {
            return Optional.ofNullable(clientService.getClients(command));
        }
        if (command.getParameters().size() == 3) {
            return Optional.ofNullable(clientService.getClientByAsset(command));
        }
        return Optional.empty();
    }

    private Optional<SlashCommandResponse> onHealthCommand(Command command) {
        if (command.getParameters().size() == 0) {
            return Optional.ofNullable(healthService.getHealth());
        } else if (command.getParameters().size() == 1) {
            return Optional.ofNullable(healthService.getHealthByEnvironment(command.getParameters().get(0)));
        }
        return Optional.empty();
    }
}
