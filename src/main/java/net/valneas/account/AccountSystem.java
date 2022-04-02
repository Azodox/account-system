package net.valneas.account;

import net.valneas.account.api.commands.AccountCommand;
import net.valneas.account.api.commands.RankCommand;
import net.valneas.account.listener.*;
import net.valneas.account.mongo.Mongo;
import net.valneas.account.permission.PermissionDatabase;
import net.valneas.account.permission.PermissionDispatcher;
import net.valneas.account.util.MongoUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class AccountSystem extends JavaPlugin {

    private Mongo mongo;
    private MongoUtil mongoUtil;
    private PermissionDatabase permissionDatabase;
    private PermissionDispatcher permissionDispatcher;

    /**
     * This field will be used to choose between finding the player in the whole proxy
     * and send him a message or if the server is not linked
     * with a BungeeCord then it's not necessary to chase him in the proxy because there isn't.
     * So anyway, in this case just use basic methods
     * @see MajorRankChangedListener
     * @see Bukkit#getOnlinePlayers()
     */
    private boolean bungeeMode;

    /**
     * Plugin initialization
     */
    @Override
    public void onEnable() {
        saveDefaultConfig();

        mongo = new Mongo(this);
        mongoUtil = new MongoUtil(this);
        permissionDatabase = new PermissionDatabase(this);
        permissionDispatcher = new PermissionDispatcher(this);

        this.permissionDispatcher.onEnable();

        registerEvents();
        getCommand("rank").setExecutor(new RankCommand(this));
        getCommand("account").setExecutor(new AccountCommand(this));

        getServer().getServicesManager().register(AccountSystem.class, this, this, ServicePriority.Normal);
        getLogger().info("Enabled!");
    }

    /**
     * Registering events
     */
    private void registerEvents() {
        registerListeners(
                new PlayerJoinListener(this),
                new PlayerQuitListener(this),
                new MajorRankChangedListener(this),
                new RankAddedListener(this),
                new RankRemovedListener(this)
        );
    }

    /**
     * Register a listener
     * @param listeners : Listener to register
     */
    private void registerListeners(Listener...listeners){
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    /**
     * Get a mongo class instance
     * @return mongo class instance
     */
    public Mongo getMongo() {
        return mongo;
    }

    /**
     * Get a mongo util class instance
     * @return mongo util class instance
     */
    public MongoUtil getMongoUtil() {
        return mongoUtil;
    }

    /**
     * Check if bungeeMode is enabled.
     * @return bungeeMode
     */
    public boolean isBungeeMode() {
        return bungeeMode;
    }

    public PermissionDatabase getPermissionDatabase() {
        return permissionDatabase;
    }

    public PermissionDispatcher getPermissionDispatcher() {
        return permissionDispatcher;
    }
}