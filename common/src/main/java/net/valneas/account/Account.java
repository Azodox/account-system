package net.valneas.account;

import java.util.List;

/**
 * @author Azodox_ (Luke)
 * 5/6/2022.
 */


public class Account extends AbstractAccount {

    public Account(String uuid, String name, String lastIp, int majorRankId, boolean moderationMode, boolean vanish, double farmingPoints, double farmingPrestige, int currentJobId, long lastJobChange, double xp, double level, double money, double points, List<Integer> ranksIds, long firstConnection, long lastConnection, long lastDisconnection, boolean superUser) {
        super(uuid, name, lastIp, majorRankId, moderationMode, vanish, farmingPoints, farmingPrestige, currentJobId, lastJobChange, xp, level, money, points, ranksIds, firstConnection, lastConnection, lastDisconnection, superUser);
    }
}
