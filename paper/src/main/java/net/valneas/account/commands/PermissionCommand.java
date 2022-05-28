package net.valneas.account.commands;

import io.github.llewvallis.commandbuilder.CommandContext;
import io.github.llewvallis.commandbuilder.ExecuteCommand;
import io.github.llewvallis.commandbuilder.OptionalArg;
import io.github.llewvallis.commandbuilder.arguments.StringSetArgument;
import net.valneas.account.AccountManager;
import net.valneas.account.AccountSystem;
import net.valneas.account.permission.PermissionDatabase;
import net.valneas.account.rank.RankUnit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PermissionCommand {

    private final AccountSystem accountSystem;

    public PermissionCommand(AccountSystem accountSystem) {
        this.accountSystem = accountSystem;
    }

    @ExecuteCommand
    public void permission(CommandContext ctx, @StringSetArgument.Arg({"add", "remove", "show", "reload", "except", "unexcept"}) String operation, String targetInput, @OptionalArg PermissionDatabase.Permission permission) {
        var target = PermissionDatabase.DatabaseParser.parse(targetInput);
        if(target == null){
            ctx.getSender().sendMessage("Invalid target");
            return;
        }

        if(operation.equals("add")){
            this.accountSystem.getPermissionDispatcher().set(target, permission.permission());
            ctx.getSender().sendMessage("Good");
            return;
        }

        if(operation.equals("remove")){
            this.accountSystem.getPermissionDispatcher().remove(target, permission.permission());
            ctx.getSender().sendMessage("Good");
            return;
        }

        if(operation.equals("reload")){
            this.accountSystem.getPermissionDispatcher().reloadPermissions(target);
            ctx.getSender().sendMessage("Permissions de '" + target + "' rechargées ✔");
            return;
        }

        if(operation.equals("except")){
            this.accountSystem.getPermissionDispatcher().addException(target, permission);
            ctx.getSender().sendMessage("Exception '" + target + "' ajoutée à '" + permission.permission() + "' ✔");
            return;
        }

        if(operation.equals("unexcept")){
            this.accountSystem.getPermissionDispatcher().removeException(target, permission);
            ctx.getSender().sendMessage("Exception '" + target + "' retirée de '" + permission.permission() + "' ✔");
            return;
        }

        if(operation.equals("show")){
            if(target instanceof Player player) {
                ctx.getSender().sendMessage(this.getUUIDShowMessage(player.getUniqueId()));
            }else if(target instanceof UUID uuid){
                ctx.getSender().sendMessage(this.getUUIDShowMessage(uuid));
            }else if(target instanceof RankUnit rankUnit){
                var rankPermissions = this.accountSystem.getPermissionDatabase().getRankPermissions(rankUnit);
                if(rankPermissions.isEmpty()){
                    ctx.getSender().sendMessage("Aucune permission n'est attribuée à ce rang"); 
                }else {
                    rankPermissions.forEach(perm -> ctx.getSender().sendMessage(perm.permission()));
                }
            }
        }
    }

    public String getUUIDShowMessage(UUID uuid){
        var accountManager = new AccountManager(this.accountSystem, null, uuid.toString());
        var rankManager = accountManager.newRankManager();
        var ranks = rankManager.getRanks();

        var uuidPermissions = this.accountSystem.getPermissionDatabase().getUUIDPermissions(uuid);
        var majorPermissions = this.accountSystem.getPermissionDatabase().getRankPermissions(rankManager.getMajorRank());
        var ranksPermissions = new HashMap<RankUnit, List<PermissionDatabase.Permission>>();

        ranks.forEach(rank -> ranksPermissions.put(rank, this.accountSystem.getPermissionDatabase().getRankPermissions(rank)));

        if(uuidPermissions.isEmpty()){
            return "Aucune permission n'est attribuée à ce joueur";
        }else{
            var builder = new StringBuilder();

            if(!majorPermissions.isEmpty()){
                builder.append("Hérité du rang majeur (").append(rankManager.getMajorRank().getColor()).append(rankManager.getMajorRank().getName()).append(ChatColor.RESET).append(") :\n");
                majorPermissions.forEach(perm -> builder.append(perm.permission()).append("\n"));
            }

            if(!ranksPermissions.isEmpty()){
                ranksPermissions.forEach((rank, permissions) -> {
                   if(permissions.isEmpty()) {
                       return;
                   }
                   builder.append("Hérité du rang ").append(rank.getColor()).append(rank.getName()).append(ChatColor.RESET).append(" :\n");
                   permissions.forEach(perm -> builder.append(perm.permission()).append("\n"));
                });
            }

            builder.append("Permissions :\n");
            uuidPermissions.forEach(perm -> builder.append(perm.permission()).append("\n"));
            return builder.toString();
        }
    }
}
