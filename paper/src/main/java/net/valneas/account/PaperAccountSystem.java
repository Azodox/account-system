package net.valneas.account;

import dev.morphia.Datastore;
import io.github.llewvallis.commandbuilder.CommandBuilder;
import io.github.llewvallis.commandbuilder.DefaultInferenceProvider;
import io.github.llewvallis.commandbuilder.ReflectionCommandCallback;
import lombok.Getter;
import net.valneas.account.commands.AccountCommand;
import net.valneas.account.commands.PermissionCommand;
import net.valneas.account.commands.RankCommand;
import net.valneas.account.commands.SetDefaultCommand;
import net.valneas.account.commands.arguments.BooleanArgument;
import net.valneas.account.commands.arguments.PermissionArgument;
import net.valneas.account.listener.*;
import net.valneas.account.mongo.Mongo;
import net.valneas.account.permission.PaperPermission;
import net.valneas.account.permission.PaperPermissionDatabase;
import net.valneas.account.permission.PaperPermissionDispatcher;
import net.valneas.account.provider.AccountSystemApiProvider;
import net.valneas.account.rank.PaperRankHandler;
import net.valneas.account.rank.RankHandler;
import net.valneas.account.rank.RankManager;
import net.valneas.account.rank.RankUnit;
import net.valneas.account.util.MongoUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class PaperAccountSystem extends JavaPlugin implements AccountSystemApi {

    private Mongo mongo;
    private MongoUtil mongoUtil;
    private @Getter Datastore datastore;
    private PaperRankHandler rankHandler;
    private PaperPermissionDatabase permissionDatabase;
    private PaperPermissionDispatcher permissionDispatcher;
    /**
     * This field will be used to choose between finding the player in the whole proxy
     * and send him a message or if the server is not linked
     * with a BungeeCord then it's not necessary to chase him in the proxy because there isn't.
     * So anyway, in this case just use basic methods
     * @see MajorRankChangedListener
     */
    private boolean bungeeMode;

    /**
     * Plugin initialization
     */
    @Override
    public void onEnable() {
        saveDefaultConfig();

        mongo = new Mongo(
                getConfig().getString("mongodb.username"),
                getConfig().getString("mongodb.authDatabase"),
                getConfig().getString("mongodb.password"),
                getConfig().getString("mongodb.host"),
                getConfig().getInt("mongodb.port"));
        mongoUtil = new MongoUtil(this);

        this.datastore = new MorphiaInitializer(this.getClass(), mongo.getMongoClient(), getConfig().getString("mongodb.database"), new String[]{"net.valneas.account", "net.valneas.account.rank"}).getDatastore();
        this.rankHandler = new PaperRankHandler(datastore);

        permissionDatabase = new PaperPermissionDatabase(this);
        permissionDispatcher = new PaperPermissionDispatcher(this);

        this.permissionDispatcher.onEnable();

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

        EventBus.getDefault().register(new MajorRankChangedListener(this));
        EventBus.getDefault().register(new RankAddedListener(this));
        EventBus.getDefault().register(new RankRemovedListener(this));

        registerEvents();
        getCommand("rank").setExecutor(new RankCommand(this));
        getCommand("account").setExecutor(new AccountCommand(this));
        DefaultInferenceProvider.getGlobal().register(PaperPermission.class, new PermissionArgument());
        DefaultInferenceProvider.getGlobal().register(boolean.class, new BooleanArgument());

        new CommandBuilder().infer(new PermissionCommand(this)).build(new ReflectionCommandCallback(new PermissionCommand(this)), getCommand("permission"));
        new CommandBuilder().infer(new SetDefaultCommand(this)).build(new ReflectionCommandCallback(new SetDefaultCommand(this)), getCommand("setdefault"));

        registerAccountSystemService();
        registerRankHandlerService();
        getLogger().info("Enabled!");
    }

    /**
     * Registering events
     */
    private void registerEvents() {
        registerListeners(
                new PlayerJoinListener(this),
                new PlayerQuitListener(this)
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

    public PaperPermissionDatabase getPermissionDatabase() {
        return permissionDatabase;
    }

    @Override
    @NotNull
    public PaperPermissionDispatcher getPermissionDispatcher() {
        return permissionDispatcher;
    }

    /**
     * Check if bungeeMode is enabled.
     * @return bungeeMode
     */
    public boolean isBungeeMode() {
        return bungeeMode;
    }

    @NotNull
    @Override
    public <T extends RankUnit> RankHandler<T> getRankHandler() {
        return (RankHandler<T>) this.rankHandler;
    }

    @NotNull
    @Override
    public <A extends AbstractAccount, R extends RankManager<? extends RankUnit>, T> AccountManager<A, R> getAccountManager(T playerType) {
        if(playerType instanceof Player player){
            return (AccountManager<A, R>) new PaperAccountManager(this, player);
        }else{
            throw new IllegalArgumentException("Wrong player type for this platform.");
        }
    }

    @Override
    public void registerAccountSystemService() {
        AccountSystemApiProvider.register(this);
        getServer().getServicesManager().register(AccountSystemApi.class, this, this, ServicePriority.Normal);
    }

    @Override
    public void registerRankHandlerService() {
        AccountSystemApiProvider.register(this.rankHandler);
        getServer().getServicesManager().register(RankHandler.class, this.rankHandler, this, ServicePriority.Normal);
    }
}
