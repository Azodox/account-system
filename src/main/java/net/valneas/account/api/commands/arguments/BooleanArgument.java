package net.valneas.account.api.commands.arguments;

import io.github.llewvallis.commandbuilder.ArgumentParseException;
import io.github.llewvallis.commandbuilder.ArgumentParser;
import io.github.llewvallis.commandbuilder.CommandContext;

import java.util.List;
import java.util.Set;

public class BooleanArgument implements ArgumentParser<Boolean> {

    @Override
    public Boolean parse(String argument, int position, CommandContext context) throws ArgumentParseException {
        try {
            return Boolean.valueOf(argument);
        } catch (Exception e) {
            throw new ArgumentParseException("Invalid boolean value: " + argument);
        }
    }

    @Override
    public Set<String> complete(List<Object> parsedArguments, String currentArgument, int position, CommandContext context) {
        return Set.of(Boolean.TRUE.toString(), Boolean.FALSE.toString());
    }
}
