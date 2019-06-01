package no.fint.slack.command.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import no.fint.event.model.Event;

@Data
public class HealthCheckResponse {
    private HealthCheckProps props;
    @JsonProperty("healthy")
    private boolean isHealthy;
    private Event event;
}