package no.fint.slack.command.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Command {

    private Commands command;
    private Commands.Sub subCommand;
    @Builder.Default private List<String> parameters = new ArrayList<>();


}
