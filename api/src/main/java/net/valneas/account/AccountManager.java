package net.valneas.account;

import dev.morphia.query.Query;
import net.valneas.account.rank.RankManager;
import net.valneas.account.rank.RankUnit;

/**
 * @author Azodox_ (Luke)
 * 13/7/2022.
 * @param <T> The type of account
 * @param <E> The type of rank manager
 */

public interface AccountManager<T extends AbstractAccount, E extends RankManager<? extends RankUnit>> {

    boolean hasAnAccount();
    Query<T> getAccountQuery();
    T getAccount();
    String getName();
    String getUuid();
    E newRankManager();
}
