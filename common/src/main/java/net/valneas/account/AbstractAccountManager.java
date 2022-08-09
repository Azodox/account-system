package net.valneas.account;

import com.google.common.base.Preconditions;
import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.experimental.filters.Filters;
import dev.morphia.query.experimental.updates.UpdateOperators;
import net.valneas.account.rank.AbstractRankManager;

public abstract class AbstractAccountManager<T extends AbstractRankManager<?, ?>> implements AccountManager<Account, T> {

    protected final Datastore datastore;
    private final String name, uuid;

    public AbstractAccountManager(Datastore datastore, String name, String uuid) {
        this.datastore = datastore;
        this.name = name;
        this.uuid = uuid;
    }

    public abstract void createAccount(int defaultRankId);

    @Override
    public boolean hasAnAccount(){
        return this.datastore.find(Account.class).filter(Filters.eq("uuid", uuid)).count() != 0;
    }

    public void updateOnLogin(){
        if(!hasAnAccount()) return;
        var query = getAccountQuery();
        net.valneas.account.Account account = query.first();

        Preconditions.checkNotNull(account, "Account not found");

        if(!account.getName().equals(name))
            query.update(UpdateOperators.set("name", name)).execute();
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
