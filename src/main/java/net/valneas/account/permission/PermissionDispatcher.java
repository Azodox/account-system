package net.valneas.account.permission;

import net.valneas.account.AccountSystem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Set;

public class PermissionDispatcher {

    private final AccountSystem accountSystem;

    public PermissionDispatcher(AccountSystem accountSystem){
        this.accountSystem = accountSystem;
    }

    public void onEnable(){
        Bukkit.getOnlinePlayers().forEach(this.accountSystem.getPermissionDatabase()::setPlayerPermission);
    }

    public void reloadPermissions() {
        this.onEnable();
    }

    public void reloadPermissions(Player player){
        this.accountSystem.getPermissionDatabase().setPlayerPermission(player);
    }

    public void set(Player player, String permission, boolean state){
        if(!state){
            permission = '-' + permission;
        }

        var dbPermission = this.accountSystem.getPermissionDatabase().get(permission);

        if(dbPermission == null){
            this.accountSystem.getPermissionDatabase().update(new PermissionDatabase.Permission(
                    permission,
                    Set.of(player.getUniqueId()),
                    null,
                    null
            ));
        }else{
            dbPermission.players().add(player.getUniqueId());
            this.accountSystem.getPermissionDatabase().update(dbPermission);
        }
        reloadPermissions(player);
    }

    public void set(Player player){
        this.accountSystem.getPermissionDatabase().setPlayerPermission(player);
    }
}