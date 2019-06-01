package no.fint.slack.command;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CommandParser {

    public static Optional<Command> parse(String commandText) {

        if (commandText ==  null) {
            return Optional.empty();
        }

        List<String> commands = Arrays.asList(commandText.split("\\s+"));

        if (commands.isEmpty()) {
            return Optional.empty();
        }
        if (commands.get(0).equals("get")) {
            return getCommand(commands);
        }
        return Optional.empty();
    }

    private static Optional<Command> getCommand(List<String> commands) {

        if (commands.size() < 2) {
            return Optional.empty();
        }
        if (commands.get(1).equals("health")) {
            return createHealthCommand(commands);
        }
        /*
        else if (commands.get(1).equals("envs")) {
            creat
        }
         */
        return Optional.empty();

    }

    private static Optional<Command> createHealthCommand(List<String> commands) {
        if (commands.size() == 2) {
            return Optional.ofNullable(GetHealthCommand.builder().all(true).build());
        }
        if (commands.size() < 3) {
            return Optional.empty();
        }
        return Optional.ofNullable(GetHealthCommand.builder().environment(commands.get(2)).build());
    }
}
