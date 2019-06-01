package no.fint.slack.command.model;

import lombok.Getter;

@Getter
public class HealthCheckProps {
    String path;
    String environment;
    String name;
}