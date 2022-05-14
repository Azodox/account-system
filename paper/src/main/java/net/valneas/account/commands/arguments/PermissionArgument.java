package net.valneas.account.commands.arguments;

import io.github.llewvallis.commandbuilder.ArgumentParser;
import io.github.llewvallis.commandbuilder.CommandContext;
import net.valneas.account.permission.PermissionDatabase;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PermissionArgument implements ArgumentParser<PermissionDatabase.Permission> {

    @Override
    public PermissionDatabase.Permission parse(String argument, int position, CommandContext context) {
        return new PermissionDatabase.Permission(argument, false, Set.of(), Set.of(), Set.of());
    }

    @Override
    public Set<String> complete(List<Object> parsedArguments, String currentArgument, int position, CommandContext context) {
        return Bukkit.getPluginManager().getPermissions().stream().map(Permission::getName).collect(Collectors.toSet());
    }
}
