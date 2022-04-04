package net.valneas.account.permission;

import net.valneas.account.AccountManager;
import net.valneas.account.AccountSystem;
import net.valneas.account.rank.RankUnit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class PermissionDispatcher {

    private final AccountSystem accountSystem;

    public PermissionDispatcher(AccountSystem accountSystem){
        this.accountSystem = accountSystem;
    }

    public void onEnable(){
        Bukkit.getOnlinePlayers().forEach(this::reloadPermissions);
    }

    public void reloadPermissions() {
        this.onEnable();
    }

    public void reloadPermissions(Object target){
        if(target instanceof Player player) {
            this.accountSystem.getPermissionDatabase().setPlayerPermission(player);
        }else if(target instanceof UUID uuid){
            this.accountSystem.getPermissionDatabase().setPlayerPermission(Bukkit.getPlayer(uuid));
        }else if(target instanceof RankUnit rankUnit){
            Bukkit.getOnlinePlayers().forEach(player -> {
                var account = new AccountManager(this.accountSystem, player);
                var rank = account.newRankManager();

                if(rank.hasRank(rankUnit)){
                    this.accountSystem.getPermissionDatabase().setPlayerPermission(player);
                }
            });
        }
    }

    public void set(Object target, String permission){
        var dbPermission = this.accountSystem.getPermissionDatabase().get(permission);

        if(dbPermission == null){
            this.accountSystem.getPermissionDatabase().update(new PermissionDatabase.Permission(
                    permission,
                    target instanceof Player player ? Set.of(player.getUniqueId()) : target instanceof UUID uuid ? Set.of(uuid) : null,
                    target instanceof RankUnit rankUnit ? Set.of(rankUnit) : null,
                    null
            ));
        }else{
            if(target instanceof Player player) {
                dbPermission.players().add(player.getUniqueId());
            }else if(target instanceof UUID uuid){
                dbPermission.players().add(uuid);
            }else if(target instanceof RankUnit rankUnit){
                dbPermission.ranks().add(rankUnit);
            }
            this.accountSystem.getPermissionDatabase().update(dbPermission);
        }
        reloadPermissions(target);
    }

    public void remove(Object target, String permission){
        var dbPermission = this.accountSystem.getPermissionDatabase().get(permission);

        if(dbPermission == null){
            return;
        }


    }

    public void set(Player player){
        this.accountSystem.getPermissionDatabase().setPlayerPermission(player);
    }
}