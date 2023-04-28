package net.valneas.account;

import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.experimental.filters.Filters;
import net.valneas.account.rank.AbstractRankManager;
import redis.clients.jedis.JedisPool;

public abstract class AbstractAccountManager<T extends AbstractRankManager<?, ?>> implements AccountManager<Account, T> {

    protected final JedisPool jedis;
    protected final Datastore datastore;
    private final String name, uuid;

    public AbstractAccountManager(JedisPool jedis, Datastore datastore, String name, String uuid) {
        this.jedis = jedis;
        this.datastore = datastore;
        this.name = name;
        this.uuid = uuid;
    }

    public abstract void createAccount(int defaultRankId);

    @Override
    public boolean hasAnAccount(){
        return this.datastore.find(Account.class).filter(Filters.eq("uuid", uuid)).count() != 0;
    }

    @Override
    public Query<Account> getAccountQuery(){
        return this.datastore.find(Account.class).filter(Filters.eq("uuid", uuid));
    }

    @Override
    public Account getAccount(){
        return getAccountQuery().first();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUuid() {
        return uuid;
    }
}
