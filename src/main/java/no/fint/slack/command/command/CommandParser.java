package no.fint.slack.command.command;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CommandParser {

    public static Optional<Command> parse(String commandText) {


        try {
            if (commandText == null) {
                return Optional.empty();
            }

            List<String> commands = Arrays.asList(commandText.split("\\s+"));

            if (commands.size() == 1) {
                return Optional.ofNullable(Command
                        .builder()
                        .command(Commands.getCommand(commands.get(0)))
                        .build());
            }

            if (commands.size() == 2) {
                return Optional.ofNullable(Command
                        .builder()
                        .command(Commands.getCommand(commands.get(0)))
                        .subCommand(Commands.Sub.getSub(commands.get(1)))
                        .build());
            }

            if (commands.size() > 2) {
                return Optional.ofNullable(Command
                        .builder()
                        .command(Commands.getCommand(commands.get(0)))
                        .subCommand(Commands.Sub.getSub(commands.get(1)))
                        .parameters(commands.subList(2, commands.size()))
                        .build());
            }
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.empty();

    }
}
