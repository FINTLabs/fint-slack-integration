package no.fint.slack.command

import no.fint.slack.command.command.CommandParser
import no.fint.slack.command.command.Commands
import spock.lang.Specification

class CommandParserSpoc extends Specification {

    def "Parse with only command "() {
        when:
        def command = CommandParser.parse("help")

        then:
        command.isPresent()
        command.get().command == Commands.HELP
    }

    def "Parse with subcommand"() {
        when:
        def command = CommandParser.parse("get health")

        then:
        command.isPresent()
        command.get().command == Commands.GET
        command.get().subCommand == Commands.Sub.HEALTH
    }

    def "Parse with parameters"() {
        when:
        def command = CommandParser.parse("get health test test")

        then:
        command.isPresent()
        command.get().command == Commands.GET
        command.get().subCommand == Commands.Sub.HEALTH
        command.get().parameters.size() == 2

    }
}
