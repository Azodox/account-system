package net.valneas.account.permission;

import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.permission.PermissionSubject;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;
import net.valneas.account.VelocityAccountManager;
import net.valneas.account.VelocityAccountSystem;
import net.valneas.account.rank.AbstractRankUnit;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * @author Azodox_ (Luke)
 * 19/6/2022.
 */

public class VelocityPermissionDatabase extends AbstractPermissionDatabase<VelocityPermission> {

    private final VelocityAccountSystem accountSystem;

    public VelocityPermissionDatabase(VelocityAccountSystem accountSystem) {
        super(accountSystem.getDatastore(), VelocityPermission.class);
        this.accountSystem = accountSystem;
    }

    public Tristate getPermissionValue(PermissionSubject subject, String permission, @Nullable PermissionFunction delegate){
        if(subject instanceof Player player){
            var perm = get(permission);
            if(perm == null)
                return delegate == null ? Tristate.UNDEFINED : delegate.getPermissionValue(permission);

            if(perm.isDefault() && !perm.getExceptIds().contains(player.getUniqueId().toString()))
                return convert(perm.getPermission());

            if (perm.getPlayers().contains(player.getUniqueId()) && !perm.getExceptIds().contains(player.getUniqueId().toString()))
                return convert(perm.getPermission());

            var accountManager = new VelocityAccountManager(this.accountSystem, player);
            var rankManager = accountManager.newRankManager();
            var rankIds = new ArrayList<>(rankManager.getRanks().stream().map(AbstractRankUnit::getId).toList());
            rankIds.add(rankManager.getMajorRank().getId());

            if(perm.getRanksIds().stream().anyMatch(id -> rankIds.contains(id) && !perm.getExceptIds().contains(id.toString())))
                return convert(perm.getPermission());
        }
        return delegate != null ? delegate.getPermissionValue(permission) : Tristate.UNDEFINED;
    }

    private Tristate convert(String permission){
        return Tristate.fromBoolean(!permission.startsWith("-"));
    }
}
