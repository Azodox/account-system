package net.valneas.account.permission;

import net.valneas.account.AccountManager;
import net.valneas.account.AccountSystem;
import net.valneas.account.rank.RankUnit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Set;
import java.util.UUID;

public class PermissionDispatcher {

    private final AccountSystem accountSystem;

    public PermissionDispatcher(AccountSystem accountSystem){
        this.accountSystem = accountSystem;
    }

    public void onEnable(){
        this.accountSystem.getPermissionDatabase().getPermissions().stream().filter(PermissionDatabase.Permission::defaults).forEach(permission -> Bukkit.getPluginManager().addPermission(new Permission(permission.permission(), "", PermissionDefault.TRUE)));
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

    public void addException(Object target, PermissionDatabase.Permission permission){
        var dbPermission = this.accountSystem.getPermissionDatabase().get(permission.permission());

        if(dbPermission == null){
            permission.exceptions().add(target);
            this.accountSystem.getPermissionDatabase().init(permission);
        }else{
            dbPermission.exceptions().add(target);
            this.accountSystem.getPermissionDatabase().update(addAccordingToObject(target, dbPermission));
        }
        reloadPermissions(target);
    }

    public void removeException(Object target, PermissionDatabase.Permission permission) {
        var dbPermission = this.accountSystem.getPermissionDatabase().get(permission.permission());

        if (dbPermission == null) {
            return;
        }

        dbPermission.exceptions().remove(target);
        this.accountSystem.getPermissionDatabase().update(addAccordingToObject(target, dbPermission));
        reloadPermissions(target);
    }

    public void setDefault(String permission){
        var dbPermission = this.accountSystem.getPermissionDatabase().get(permission);

        if (dbPermission == null) {
            return;
        }

        this.accountSystem.getPermissionDatabase().update(new PermissionDatabase.Permission(
                dbPermission.permission(),
                true,
                dbPermission.players(),
                dbPermission.ranks(),
                dbPermission.exceptions()
        ));
        reloadPermissions();
    }

    public void unsetDefault(String permission){
        var dbPermission = this.accountSystem.getPermissionDatabase().get(permission);

        if (dbPermission == null) {
            return;
        }

        this.accountSystem.getPermissionDatabase().update(new PermissionDatabase.Permission(
                dbPermission.permission(),
                false,
                dbPermission.players(),
                dbPermission.ranks(),
                dbPermission.exceptions()
        ));
        reloadPermissions();
    }

    public void set(Object target, PermissionDatabase.Permission permission){
        var dbPermission = this.accountSystem.getPermissionDatabase().get(permission.permission());

        if(dbPermission == null){
            this.accountSystem.getPermissionDatabase().init(addAccordingToObject(target, permission));
        }else{
            this.accountSystem.getPermissionDatabase().update(addAccordingToObject(target, dbPermission));
        }
        reloadPermissions(target);
    }

    public void set(Object target, String permission){
        var dbPermission = this.accountSystem.getPermissionDatabase().get(permission);

        if(dbPermission == null){
            this.accountSystem.getPermissionDatabase().init(new PermissionDatabase.Permission(
                    permission,
                    false,
                    target instanceof Player player ? Set.of(player.getUniqueId()) : target instanceof UUID uuid ? Set.of(uuid) : Set.of(),
                    target instanceof RankUnit rankUnit ? Set.of(rankUnit) : Set.of(),
                    Set.of()
            ));
        }else{
            this.accountSystem.getPermissionDatabase().update(addAccordingToObject(target, dbPermission));
        }
        reloadPermissions(target);
    }

    public void remove(Object target, String permission){
        var dbPermission = this.accountSystem.getPermissionDatabase().get(permission);

        if(dbPermission == null){
            return;
        }

        this.accountSystem.getPermissionDatabase().update(removeAccordingToObject(target, dbPermission));
        reloadPermissions(target);
    }

    public void set(Player player){
        this.accountSystem.getPermissionDatabase().setPlayerPermission(player);
    }

    private PermissionDatabase.Permission addAccordingToObject(Object target, PermissionDatabase.Permission permission){
        if(target instanceof Player player) {
            permission.players().add(player.getUniqueId());
        }else if(target instanceof UUID uuid){
            permission.players().add(uuid);
        }else if(target instanceof RankUnit rankUnit){
            permission.ranks().add(rankUnit);
        }
        return permission;
    }

    private PermissionDatabase.Permission removeAccordingToObject(Object target, PermissionDatabase.Permission permission){
        if(target instanceof Player player) {
            permission.players().remove(player.getUniqueId());
        }else if(target instanceof UUID uuid){
            permission.players().remove(uuid);
        }else if(target instanceof RankUnit rankUnit){
            permission.ranks().remove(rankUnit);
        }
        return permission;
    }
}