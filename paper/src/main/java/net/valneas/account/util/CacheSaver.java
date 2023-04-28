package net.valneas.account.util;

import com.google.common.base.Preconditions;
import dev.morphia.query.experimental.filters.Filters;
import net.valneas.account.Account;
import net.valneas.account.PaperAccountSystem;
import org.bukkit.Bukkit;

import java.util.UUID;

/**
 * @author Azodox_ (Luke)
 * 7/8/2022.
 */

public class CacheSaver {

    private final PaperAccountSystem paperAccountSystem;

    public CacheSaver(PaperAccountSystem paperAccountSystem) {
        this.paperAccountSystem = paperAccountSystem;
    }

    public void onJoin(Account account) {
        var jedis = paperAccountSystem.getJedisPool().getResource();

        if (!jedis.exists("account#" + account.getUuid()))
            this.save(account);
        else {
            jedis.hset("account#" + account.getUuid(), "name", account.getName());
            jedis.persist("account#" + account.getUuid());
            jedis.persist("ranks#" + account.getUuid());
        }
        jedis.hset("account#" + account.getUuid(), "last-connection", String.valueOf(System.currentTimeMillis()));
        jedis.hset("account#" + account.getUuid(), "last-ip", PlayerUtil.getIp(Bukkit.getPlayer(UUID.fromString(account.getUuid()))));
        jedis.close();
    }

    public void save(Account account) {
        var jedis = paperAccountSystem.getJedisPool().getResource();

        jedis.hset("account#" + account.getUuid(), account.serialize());
        jedis.sadd("ranks#" + account.getUuid(), account.getRanksIds().stream().map(String::valueOf).toArray(String[]::new));

        jedis.close();
    }

    public void saveInDB(UUID uuid) {
        var jedis = paperAccountSystem.getJedisPool().getResource();

        var datastore = paperAccountSystem.getDatastore();
        var account = Preconditions.checkNotNull(datastore.find(Account.class).filter(Filters.eq("uuid", uuid.toString())).first(), "account doesn't exist");

        datastore.merge(
                Account.deserialize(
                        account.get_id(),
                        jedis.hgetAll("account#" + uuid.toString()),
                        jedis.smembers("ranks#" + uuid).stream().map(Integer::parseInt).toList()
                )
        );

        jedis.expire("account#" + uuid, 86400 * 7 /*1 week *winks**/);
        jedis.expire("ranks#" + uuid, 86400 * 7 /*1 week *winks**/);

        jedis.close();
    }
}
