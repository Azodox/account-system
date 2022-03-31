package net.valneas.account;

import net.valneas.account.api.commands.AccountCommand;
import net.valneas.account.api.commands.RankCommand;
import net.valneas.account.listener.*;
import net.valneas.account.mongo.Mongo;
import net.valneas.account.util.MongoUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class AccountSystem extends JavaPlugin {

    private Mongo mongo;
    private MongoUtil mongoUtil;

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

        File spigotYml = new File("../../../" + getDataFolder(), "spigot.yml");
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(spigotYml);
        bungeeMode = yml.getBoolean("settings.bungeecord");

        if(bungeeMode){
            getLogger().info("---------------------------------------------");
            getLogger().info("Warning: field in spigot.yml called bungeecord is enabled!");
            getLogger().info("Switching to bungeecord mode...");
            getLogger().info("Now the system will act like if there is a bungeecord linked with the server.");
            getLogger().info("If it's not, please disable this field or some functionalities won't be available.");
            getLogger().info("---------------------------------------------");
        }

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
}
