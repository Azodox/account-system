package net.valneas.account.rank;

import dev.morphia.query.Query;

/**
 * @author Azodox_ (Luke)
 * 13/7/2022.
 */

public interface RankHandler<T extends RankUnit> {

    Query<T> getAllRanksQuery();
    T getDefaultRank();
    Query<T> getById(int id);
    Query<T> getByPower(int power);
    Query<T> getByName(String name);
    Query<T> getByCommandArg(String arg);
}
