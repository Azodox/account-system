package net.valneas.account.permission;

import net.valneas.account.AccountSystem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PermissionDispatcher {

    private final AccountSystem accountSystem;

    public PermissionDispatcher(AccountSystem accountSystem){
        this.accountSystem = accountSystem;
    }

    public void onEnable(){
        Bukkit.getOnlinePlayers().forEach(this.accountSystem.getPermissionDatabase()::setPlayerPermission);
    }

    public void set(Player player){
        this.accountSystem.getPermissionDatabase().setPlayerPermission(player);
    }
}