package no.fint.slack.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetHealthCommand implements Command {
    private String environment;
    @Builder.Default private boolean all = false;

}

