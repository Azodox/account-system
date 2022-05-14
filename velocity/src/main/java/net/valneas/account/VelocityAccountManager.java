package net.valneas.account;

import com.mongodb.client.MongoDatabase;
import com.velocitypowered.api.proxy.Player;

/**
 * @author Azodox_ (Luke)
 * 14/5/2022.
 */

public class VelocityAccountManager extends AbstractAccountManager{

    private final VelocityAccountSystem plugin;

    public VelocityAccountManager(VelocityAccountSystem plugin, Player player) {
        super(plugin.getMongo().getMongoClient(), player.getUsername(), player.getUniqueId().toString());
        this.plugin = plugin;
    }

    @Override
    protected MongoDatabase getDatabase() {
        return mongo.getDatabase(plugin.getConfig().getString("MongoDB.database"));
    }
}
