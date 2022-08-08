package net.valneas.account;

import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Azodox_ (Luke)
 * 5/6/2022.
 */


public class Account extends AbstractAccount {

    public Account(String uuid, String name, String lastIp, int majorRankId, boolean moderationMode, boolean vanish, double farmingPoints, double farmingPrestige, int currentJobId, long lastJobChange, double xp, double level, double money, double points, List<Integer> ranksIds, long firstConnection, long lastConnection, long lastDisconnection, boolean superUser) {
        super(uuid, name, lastIp, majorRankId, moderationMode, vanish, farmingPoints, farmingPrestige, currentJobId, lastJobChange, xp, level, money, points, ranksIds, firstConnection, lastConnection, lastDisconnection, superUser);
    }

    public Account(ObjectId _id, String uuid, String name, String lastIp, int majorRankId, boolean moderationMode, boolean vanish, double farmingPoints, double farmingPrestige, int currentJobId, long lastJobChange, double xp, double level, double money, double points, List<Integer> ranksIds, long firstConnection, long lastConnection, long lastDisconnection, boolean superUser) {
        super(_id, uuid, name, lastIp, majorRankId, moderationMode, vanish, farmingPoints, farmingPrestige, currentJobId, lastJobChange, xp, level, money, points, ranksIds, firstConnection, lastConnection, lastDisconnection, superUser);
    }

    public boolean hasAccessToJobPower() {
        return true; // TODO : Temporary
    }

    public Map<String, String> serialize() {
        var map = new HashMap<String, String>();
        map.put("uuid", this.getUuid());
        map.put("name", this.getName());
        map.put("last-ip", this.getLastIp());
        map.put("major-rank", String.valueOf(this.getMajorRankId()));
        map.put("moderation-mode", String.valueOf(this.isModerationMode()));
        map.put("farming-points", String.valueOf(this.getFarmingPoints()));
        map.put("farming-prestige", String.valueOf(this.getFarmingPrestige()));
        map.put("xp", String.valueOf(this.getXp()));
        map.put("level", String.valueOf(this.getLevel()));
        map.put("money", String.valueOf(this.getMoney()));
        map.put("points", String.valueOf(this.getPoints()));
        map.put("ranks", "ranks#" + this.getUuid());
        map.put("first-connection", String.valueOf(this.getFirstConnection()));
        map.put("last-connection", String.valueOf(this.getLastConnection()));
        map.put("last-disconnection", String.valueOf(this.getLastDisconnection()));
        map.put("super-user", String.valueOf(this.isSuperUser()));
        map.put("vanish", String.valueOf(this.isVanish()));
        map.put("last-job-change", String.valueOf(this.getLastJobChange()));
        map.put("current-job", String.valueOf(this.getCurrentJobId()));
        return map;
    }

    public static Account deserialize(ObjectId _id, Map<String, String> map, List<Integer> ranks) {
        return new Account(
                _id,
                map.get("uuid"),
                map.get("name"),
                map.get("last-ip"),
                Integer.parseInt(map.get("major-rank")),
                Boolean.parseBoolean(map.get("moderation-mode")),
                Boolean.parseBoolean(map.get("vanish")),
                Double.parseDouble(map.get("farming-points")),
                Double.parseDouble(map.get("farming-prestige")),
                Integer.parseInt(map.get("current-job")),
                Long.parseLong(map.get("last-job-change")),
                Double.parseDouble(map.get("xp")),
                Double.parseDouble(map.get("level")),
                Double.parseDouble(map.get("money")),
                Double.parseDouble(map.get("points")),
                ranks,
                Long.parseLong(map.get("first-connection")),
                Long.parseLong(map.get("last-connection")),
                Long.parseLong(map.get("last-disconnection")),
                Boolean.parseBoolean(map.get("super-user"))
        );
    }
}
