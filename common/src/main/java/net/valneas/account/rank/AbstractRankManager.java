package net.valneas.account.rank;

import com.google.common.base.Preconditions;
import com.mongodb.client.result.UpdateResult;
import dev.morphia.query.experimental.filters.Filters;
import dev.morphia.query.experimental.updates.UpdateOperators;
import net.valneas.account.AbstractAccountManager;
import net.valneas.account.Account;

import java.util.List;

/**
 * @author Azodox_ (Luke)
 * 5/6/2022.
 */

public abstract class AbstractRankManager {

    protected final AbstractAccountManager accountManager;

    public AbstractRankManager(AbstractAccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public UpdateResult setMajorRank(int rankPower){
        return accountManager.getAccountQuery().update(UpdateOperators.set("major-rank", rankPower)).execute();
    }

    /**
     * Add a rank to the account. This method won't call any events.
     * @param rankPower The rank power of the rank to add.
     * @return The result of the update.
     */
    public UpdateResult addRank(int rankPower){
        return accountManager.getAccountQuery().update(UpdateOperators.addToSet("ranks", rankPower)).execute();
    }

    public UpdateResult removeRank(int rankPower){
        var query = accountManager.getAccountQuery();
        Account account = query.first();
        Preconditions.checkNotNull(account, "Account must exist");

        return query.update(UpdateOperators.pullAll("ranks", List.of(rankPower))).execute();
    }

    public abstract boolean hasExactMajorRank(int rankId);
    public abstract boolean hasExactRank(int rankId);
    public abstract boolean hasExactMajorOrNotRank(int rankId);
    public abstract boolean hasRank(int rankPower);
    public abstract boolean hasAtLeast(int rankPower);

    public boolean hasMajorRank(){
        return accountManager.getAccountQuery().filter(Filters.exists("major-rank")).count() != 0;
    }

    public boolean hasRanks(){
        return !accountManager.getAccount().getRanksIds().isEmpty();
    }

    public abstract AbstractRankUnit getMajorRank();
    public abstract List<AbstractRankUnit> getRanks();
}