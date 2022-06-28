package net.valneas.account;

import com.velocitypowered.api.proxy.Player;
import net.valneas.account.rank.VelocityRankManager;

import java.util.ArrayList;

/**
 * @author Azodox_ (Luke)
 * 14/5/2022.
 */

public class VelocityAccountManager extends AbstractAccountManager {

    private final VelocityAccountSystem plugin;

    public VelocityAccountManager(VelocityAccountSystem plugin, Player player) {
        super(new MorphiaInitializer(plugin.getClass(), plugin.getMongo().getMongoClient(), plugin.getConfig().getString("MongoDB.database"), new String[]{"net.valneas.account"}).getDatastore(), player.getUsername(), player.getUniqueId().toString());
        this.plugin = plugin;
    }

    @Override
    public void createAccount(int defaultRankId) {
        if(hasAnAccount())
            return;

        var account = new Account(
                this.getUuid(),
                this.getName(),
                "",
                defaultRankId,
                false,
                false,
                0.0d,
                0.0d,
                0.0d,
                0.0d,
                0.0d,
                0.0d,
                new ArrayList<>(),
                0L,
                0L,
                0L,
                false
        );

        plugin.getDatastore().save(account);
    }

    public VelocityRankManager newRankManager(){
        return new VelocityRankManager(plugin.getRankHandler(), this);
    }
}
