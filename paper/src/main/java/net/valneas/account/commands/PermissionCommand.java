package net.valneas.account.commands;

import io.github.llewvallis.commandbuilder.CommandContext;
import io.github.llewvallis.commandbuilder.ExecuteCommand;
import io.github.llewvallis.commandbuilder.OptionalArg;
import io.github.llewvallis.commandbuilder.arguments.StringSetArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.valneas.account.PaperAccountManager;
import net.valneas.account.PaperAccountSystem;
import net.valneas.account.permission.PaperPermission;
import net.valneas.account.permission.PaperPermissionDatabase;
import net.valneas.account.rank.PaperRankUnit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PermissionCommand {

    private final PaperAccountSystem accountSystem;

    public PermissionCommand(PaperAccountSystem accountSystem) {
        this.accountSystem = accountSystem;
    }

    @ExecuteCommand
    public void permission(CommandContext ctx, @StringSetArgument.Arg({"add", "remove", "show", "reload", "except", "unexcept"}) String operation, String targetInput, @OptionalArg PaperPermission permission) {
        var parser = new PaperPermissionDatabase.DatabaseParser(accountSystem);
        var target = parser.parse(targetInput);
        if(target == null){
            ctx.getSender().sendMessage("Invalid target");
            return;
        }

        if(operation.equals("add")){
            ctx.getSender().sendMessage("Modifié(s) : " + this.accountSystem.getPermissionDispatcher().addPermissionToObject(target, permission.getPermission()).getModifiedCount());
            return;
        }

        if(operation.equals("remove")){
            ctx.getSender().sendMessage("Modifié(s) : " + this.accountSystem.getPermissionDispatcher().removePermissionFromObject(target, permission.getPermission()).getModifiedCount());
            return;
        }

        if(operation.equals("reload")){
            this.accountSystem.getPermissionDispatcher().reloadPermissions(target);
            ctx.getSender().sendMessage("Permissions de '" + target + "' rechargées ✔");
            return;
        }

        if(operation.equals("except")){
            ctx.getSender().sendMessage("Exception '" + target + "' ajoutée à '" + permission.getPermission() + "' ✔");
            ctx.getSender().sendMessage("Modifié(s) : " + this.accountSystem.getPermissionDispatcher().addException(target, permission.getPermission()).getModifiedCount());
            return;
        }

        if(operation.equals("unexcept")){
            ctx.getSender().sendMessage("Exception '" + target + "' retirée de '" + permission + "' ✔");
            ctx.getSender().sendMessage("Modifié(s) : " + this.accountSystem.getPermissionDispatcher().removeException(target, permission.getPermission()).getModifiedCount());
            return;
        }

        if(operation.equals("show")){
            if(target instanceof Player player) {
                ctx.getSender().sendMessage(this.getUUIDShowMessage(player.getUniqueId()));
            }else if(target instanceof UUID uuid){
                ctx.getSender().sendMessage(this.getUUIDShowMessage(uuid));
            }else if(target instanceof PaperRankUnit rankUnit){
                var rankPermissions = this.accountSystem.getPermissionDatabase().getRankPermissions(rankUnit);
                if(rankPermissions.isEmpty()){
                    ctx.getSender().sendMessage("Aucune permission n'est attribuée à ce rang"); 
                }else {
                    rankPermissions.forEach(perm -> ctx.getSender().sendMessage(perm.getPermission()));
                }
            }
        }
    }

    public Component getUUIDShowMessage(UUID uuid){
        var accountManager = new PaperAccountManager(this.accountSystem, null, uuid.toString(), accountSystem.getJedisPool());
        var rankManager = accountManager.newRankManager();
        var ranks = rankManager.getRanks();

        var uuidPermissions = this.accountSystem.getPermissionDatabase().getUUIDPermissions(uuid);
        var majorRank = rankManager.getMajorRank();
        var majorPermissions = this.accountSystem.getPermissionDatabase().getRankPermissions(majorRank);
        var ranksPermissions = new HashMap<PaperRankUnit, List<PaperPermission>>();

        ranks.forEach(rank -> ranksPermissions.put(rank, this.accountSystem.getPermissionDatabase().getRankPermissions(rank)));

        var defaultPermissions = this.accountSystem.getPermissionDatabase().getDefaultPermissions(uuid).stream().filter(permission -> !majorPermissions.contains(permission) && ranksPermissions.values().stream().noneMatch(permissions -> permissions.contains(permission))).toList();
        if(uuidPermissions.isEmpty() && majorPermissions.isEmpty() && ranksPermissions.isEmpty()){
            return Component.text("Aucune permission n'est attribuée à ce joueur");
        }else{
            var component = Component.text();

            if(!majorPermissions.isEmpty()){
                component
                        .append(Component.text("Hérité du rang majeur ("))
                        .append(majorRank.name().color(majorRank.color()))
                        .resetStyle()
                        .append(Component.text(") :\n"));
                component.append(Component.join(JoinConfiguration.newlines(), majorPermissions.stream().map(perm -> Component.text(perm.getPermission())).toList()));
                component.append(Component.newline());
            }

            if(!ranksPermissions.isEmpty()){
                ranksPermissions.forEach((rank, permissions) -> {
                   if(permissions.isEmpty()) {
                       return;
                   }
                   component
                           .append(Component.text("Hérité du rang "))
                           .append(rank.name()).color(rank.color())
                           .resetStyle()
                           .append(Component.newline());
                   component.append(Component.join(JoinConfiguration.newlines(), permissions.stream().map(permission -> Component.text(permission.getPermission())).toList()));
                   component.append(Component.newline());
                });
            }

            if(!uuidPermissions.isEmpty() || !defaultPermissions.isEmpty()){
                component.append(Component.text("Permissions :\n"));
                component.append(Component.join(JoinConfiguration.newlines(), defaultPermissions.stream().map(permission -> Component.text(permission.getPermission()).append(Component.text(" (défaut)").color(NamedTextColor.AQUA))).toList()));
                component.append(Component.newline());
                component.append(Component.join(JoinConfiguration.newlines(), uuidPermissions.stream().map(permission -> Component.text(permission.getPermission())).toList()));
            }
            return component.build();
        }
    }
}
