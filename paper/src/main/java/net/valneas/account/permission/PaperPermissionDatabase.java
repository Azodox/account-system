package net.valneas.account.permission;

import net.valneas.account.PaperAccountManager;
import net.valneas.account.PaperAccountSystem;
import net.valneas.account.rank.PaperRankUnit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PaperPermissionDatabase extends AbstractPermissionDatabase<PaperPermission> {

    private final PaperAccountSystem accountSystem;

    public PaperPermissionDatabase(PaperAccountSystem accountSystem){
        super(accountSystem.getDatastore(), PaperPermission.class);
        this.accountSystem = accountSystem;
    }

    public void setPlayerPermission(Player player){
        var account = new PaperAccountManager(this.accountSystem, player, accountSystem.getJedisPool());
        var rank = account.newRankManager();

        player.getEffectivePermissions()
                .stream().map(PermissionAttachmentInfo::getAttachment)
                .toList().stream().filter(Objects::nonNull).forEach(PermissionAttachment::remove);


        List<PaperPermission> permissions = new ArrayList<>();

        permissions.addAll(this.getRankPermissions(rank.getMajorRank()));
        rank.getRanks().forEach(rankUnit -> permissions.addAll(this.getRankPermissions(rankUnit)));
        permissions.addAll(this.getUUIDPermissions(player.getUniqueId()));

        var attachment = player.addAttachment(this.accountSystem);
        permissions.forEach(permission -> attachment.setPermission(permission.getPermission().replace("-", ""), !permission.getPermission().startsWith("-")));
    }

    public List<PaperPermission> getRankPermissions(PaperRankUnit rank){
        return this.getPermissions().stream()
                .filter(permission -> permission.getRanksIds().contains(rank.getId())).filter(permission -> {
            if(permission.getExceptions() == null)
                return true;
            return !permission.getExceptions().contains(rank);
        }).toList();
    }

    public List<PaperPermission> getUUIDPermissions(UUID uuid){
        return this.getPermissions().stream().filter(permission -> {
            if(permission.getPlayers() == null)
                return false;
            return permission.getPlayers().contains(uuid);
        }).filter(permission -> {
            if(permission.getExceptions() == null)
                return true;
            return !permission.getExceptions().contains(uuid);
        }).toList();
    }

    public List<PaperPermission> getDefaultPermissions(UUID uuid){
        return this.getPermissions().stream().filter(AbstractPermission::isDefault).filter(permission -> {
            if(permission.getPlayers() == null)
                return true;
            return !permission.getPlayers().contains(uuid);
        }).filter(permission -> {
            if(permission.getExceptions() == null)
                return true;
            return !permission.getExceptions().contains(uuid);
        }).toList();
    }

    public static class DatabaseParser {

        private final PaperAccountSystem accountSystem;

        public DatabaseParser(PaperAccountSystem accountSystem) {
            this.accountSystem = accountSystem;
        }

        public Object parse(String value) {
            if (value == null) return null;

            if (accountSystem.getRankHandler().getByCommandArg(value).first() != null) {
                return accountSystem.getRankHandler().getByCommandArg(value).first();
            }else {
                try {
                    return UUID.fromString(value);
                } catch (IllegalArgumentException e1) {
                    try {
                        return Bukkit.getPlayer(value);
                    } catch (NullPointerException e2) {
                        return null;
                    }
                }
            }
        }

        public static String parse(Object value){
            if(value == null) return null;

            if(value instanceof PaperRankUnit){
                return String.valueOf(((PaperRankUnit) value).getId());
            } else if(value instanceof UUID){
                return value.toString();
            } else {
                return null;
            }
        }
    }
}