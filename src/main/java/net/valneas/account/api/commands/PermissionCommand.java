package net.valneas.account.api.commands;

import net.valneas.account.AccountSystem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermissionCommand implements CommandExecutor {

    private final AccountSystem accountSystem;

    public PermissionCommand(AccountSystem accountSystem) {
        this.accountSystem = accountSystem;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (args.length == 0) {
            return true;
        }

        if(args[0].equalsIgnoreCase("add")){
            if(args.length > 3){
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if(target == null){
                return true;
            }

            this.accountSystem.getPermissionDispatcher().set(target, args[2], true);
            sender.sendMessage("Good");
            return true;
        }

        if(args.length >= 2){
            return true;
        }

        if(args[0].equalsIgnoreCase("reload")){
            this.accountSystem.getPermissionDispatcher().reloadPermissions();
            sender.sendMessage("Permissions rechargées ✔");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(target != null){
            target.getEffectivePermissions().forEach(permission -> sender.sendMessage(permission.getPermission() + ", " + permission.getValue()));
        }

        return false;
    }
}
