package net.valneas.account;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.morphia.Datastore;
import lombok.Getter;
import net.valneas.account.listener.PermissionSetupListener;
import net.valneas.account.mongo.Mongo;
import net.valneas.account.permission.VelocityPermissionDatabase;
import net.valneas.account.rank.VelocityRankHandler;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Plugin(id = "@id@", name = "@name@", version = "@version@")
public class VelocityAccountSystem {

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private Mongo mongo;
    private @Getter Datastore datastore;
    private @Getter VelocityPermissionDatabase permissionDatabase;
    private @Getter VelocityRankHandler rankHandler;
    private TomlParseResult config;

    @Inject
    public VelocityAccountSystem(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;

        if(!dataDirectory.toFile().exists()) {
            dataDirectory.toFile().mkdirs();
        }

        this.dataDirectory = dataDirectory;
        logger.info("Account System plugin for Velocity successfully loaded. v@version@");
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) throws IOException {
        this.initConfig();

        this.mongo = new Mongo(
                getConfig().getString("MongoDB.username"),
                getConfig().getString("MongoDB.authDatabase"),
                getConfig().getString("MongoDB.password"),
                getConfig().getString("MongoDB.host"),
                getConfig().getLong("MongoDB.port").intValue());
        this.datastore = new MorphiaInitializer(this.getClass(), this.mongo.getMongoClient(), getConfig().getString("MongoDB.database"), new String[]{"net.valneas.account"}).getDatastore();
        this.rankHandler = new VelocityRankHandler(this.getDatastore());
        this.permissionDatabase = new VelocityPermissionDatabase(this);

        server.getEventManager().register(this, new PermissionSetupListener(this));
    }

    private void initConfig() throws IOException {
        var baseConfig = new File("config.toml");
        var config = new File(dataDirectory.toFile(), "config.toml");

        if(!config.exists()) {
            config.createNewFile();
        }

        if(!FileUtils.contentEquals(config, baseConfig) && config.length() != 0){
            logger.info("File '" + config.getPath() + "' content doesn't equals to '" + baseConfig.getPath() + "' and is not empty, skipping files copy.");
        }else {
            InputStream in = getClass().getResourceAsStream("/" + config.getName());
            FileOutputStream out = new FileOutputStream(config);

            byte[] buf = new byte[1024];
            int n;

            while ((n = in.read(buf)) >= 0) {
                out.write(buf, 0, n);
            }

            in.close();
            out.close();
            logger.info("File '" + config.getPath() + "' copied from resources.");
        }

        this.config = Toml.parse(Paths.get(this.getDataDirectory() + File.separator + config.getName()));
        this.config.errors().forEach(error -> System.err.println(error.toString()));
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }

    public TomlParseResult getConfig() {
        return config;
    }

    public Mongo getMongo() {
        return mongo;
    }
}
