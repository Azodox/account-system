package net.valneas.account.rank;


import com.mongodb.client.result.UpdateResult;

import java.util.List;

/**
 * @author Azodox_ (Luke)
 * 13/7/2022
 * @param <T> The type of rank unit.
 */

public interface RankManager<T extends RankUnit> {

    UpdateResult setMajorRank(int rankId);
    <E> UpdateResult setMajorRank(int rankId, E sender, boolean event);

    /**
     * Add a rank to the account. This method won't call any events.
     * @param rankId The rank id of the rank to add.
     * @return The result of the update.
     */
    UpdateResult addRank(int rankId);
    <E> UpdateResult addRank(int rankId, E sender, boolean event);
    UpdateResult removeRank(int rankPower);
    <E> UpdateResult removeRank(int rankPower, E sender, boolean event);
    boolean hasExactMajorRank(int rankId);
    boolean hasExactRank(int rankId);
    boolean hasExactMajorOrNotRank(int rankId);
    boolean hasRank(int rankPower);
    boolean hasAtLeast(int rankPower);
    T getMajorRank();
    List<T> getRanks();
}
