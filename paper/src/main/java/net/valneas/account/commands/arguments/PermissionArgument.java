package net.valneas.account.commands.arguments;

import io.github.llewvallis.commandbuilder.ArgumentParser;
import io.github.llewvallis.commandbuilder.CommandContext;
import net.valneas.account.permission.Permission;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PermissionArgument implements ArgumentParser<Permission> {

    @Override
    public Permission parse(String argument, int position, CommandContext context) {
        return new Permission(argument, false, Set.of(), Set.of(), Set.of());
    }

    @Override
    public Set<String> complete(List<Object> parsedArguments, String currentArgument, int position, CommandContext context) {
        return Bukkit.getPluginManager().getPermissions().stream().map(org.bukkit.permissions.Permission::getName).collect(Collectors.toSet());
    }
}
