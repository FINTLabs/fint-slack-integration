package no.fint.slack.command.command;

public enum Commands {
    GET,
    HELP;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public static Commands getCommand(String command) {
        return valueOf(command.toUpperCase());
    }

    public enum Sub {
        HEALTH,
        ASSETS,
        ENVS,
        CLIENTS;

        @Override
        public String toString() {
            return name().toLowerCase();
        }

        public static Commands.Sub getSub(String command) {
            return valueOf(command.toUpperCase());
        }
    }


}
