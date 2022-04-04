package net.valneas.account.api.commands;

import io.github.llewvallis.commandbuilder.CommandContext;
import io.github.llewvallis.commandbuilder.ExecuteCommand;
import io.github.llewvallis.commandbuilder.OptionalArg;
import io.github.llewvallis.commandbuilder.arguments.StringSetArgument;
import net.valneas.account.AccountSystem;
import net.valneas.account.permission.PermissionDatabase;
import net.valneas.account.rank.RankUnit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PermissionCommand {

    private final AccountSystem accountSystem;

    public PermissionCommand(AccountSystem accountSystem) {
        this.accountSystem = accountSystem;
    }

    @ExecuteCommand
    public void permission(CommandContext ctx, @StringSetArgument.Arg({"add", "remove", "show", "reload"}) String operation, String targetInput, @OptionalArg PermissionDatabase.Permission permission) {
        var target = PermissionDatabase.DatabaseParser.parse(targetInput);
        if(target == null){
            return;
        }

        if(operation.equalsIgnoreCase("add")){
            this.accountSystem.getPermissionDispatcher().set(target, permission.permission());
            ctx.getSender().sendMessage("Good");
            return;
        }

        if(operation.equalsIgnoreCase("remove")){

        }

        if(operation.equalsIgnoreCase("reload")){
            this.accountSystem.getPermissionDispatcher().reloadPermissions(target);
            ctx.getSender().sendMessage("Permissions de '" + target + "' rechargées ✔");
            return;
        }

        if(operation.equalsIgnoreCase("show")){
            if(target instanceof Player player) {
                this.accountSystem.getPermissionDatabase().getUUIDPermissions(player.getUniqueId()).forEach(perm -> ctx.getSender().sendMessage(perm.permission()));
            }else if(target instanceof UUID uuid){
                this.accountSystem.getPermissionDatabase().getUUIDPermissions(uuid).forEach(perm -> ctx.getSender().sendMessage(perm.permission()));
            }else if(target instanceof RankUnit rankUnit){
                this.accountSystem.getPermissionDatabase().getRankPermissions(rankUnit).forEach(perm -> ctx.getSender().sendMessage(perm.permission()));
            }
        }
    }
}
