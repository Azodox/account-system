package net.valneas.account;

import dev.morphia.annotations.*;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * @author Azodox_ (Luke)
 * 14/7/2022.
 */

@Entity(value = "accounts", discriminator = "account")
@Indexes({
        @Index(fields = @Field("name")),
        @Index(fields = @Field("uuid"), options = @IndexOptions(unique = true))
})
public abstract class AbstractAccount {

    @Id
    private @Getter ObjectId _id;

    @Property
    private @Getter
    final String uuid, name;

    @Property("last-ip")
    private @Getter final String lastIp;

    @Property("major-rank")
    private @Getter final int majorRankId;

    @Property("moderation-mod")
    private @Getter final boolean moderationMode;

    @Property("vanish")
    private @Getter final boolean vanish;

    @Property("farming-points")
    private @Getter final double farmingPoints;

    @Property("farming-prestige")
    private @Getter final double farmingPrestige;

    @Property("current-job")
    private @Getter final int currentJobId;

    @Property("last-job-change")
    private @Getter final long lastJobChange;

    @Property
    private @Getter final double xp, level, money, points;

    @Property("ranks")
    private @Getter final List<Integer> ranksIds;

    @Property("first-connection")
    private @Getter final long firstConnection;

    @Property("last-connection")
    private @Getter final long lastConnection;

    @Property("last-disconnection")
    private @Getter final long lastDisconnection;

    @Property("super-user")
    private @Getter final boolean superUser;

    public AbstractAccount(String uuid, String name, String lastIp, int majorRankId, boolean moderationMode, boolean vanish, double farmingPoints, double farmingPrestige, int currentJobId, long lastJobChange, double xp, double level, double money, double points, List<Integer> ranksIds, long firstConnection, long lastConnection, long lastDisconnection, boolean superUser) {
        this.uuid = uuid;
        this.name = name;
        this.lastIp = lastIp;
        this.majorRankId = majorRankId;
        this.moderationMode = moderationMode;
        this.vanish = vanish;
        this.farmingPoints = farmingPoints;
        this.farmingPrestige = farmingPrestige;
        this.currentJobId = currentJobId;
        this.lastJobChange = lastJobChange;
        this.xp = xp;
        this.level = level;
        this.money = money;
        this.points = points;
        this.ranksIds = ranksIds;
        this.firstConnection = firstConnection;
        this.lastConnection = lastConnection;
        this.lastDisconnection = lastDisconnection;
        this.superUser = superUser;
    }

    public AbstractAccount(ObjectId _id, String uuid, String name, String lastIp, int majorRankId, boolean moderationMode, boolean vanish, double farmingPoints, double farmingPrestige, int currentJobId, long lastJobChange, double xp, double level, double money, double points, List<Integer> ranksIds, long firstConnection, long lastConnection, long lastDisconnection, boolean superUser) {
        this._id = _id;
        this.uuid = uuid;
        this.name = name;
        this.lastIp = lastIp;
        this.majorRankId = majorRankId;
        this.moderationMode = moderationMode;
        this.vanish = vanish;
        this.farmingPoints = farmingPoints;
        this.farmingPrestige = farmingPrestige;
        this.currentJobId = currentJobId;
        this.lastJobChange = lastJobChange;
        this.xp = xp;
        this.level = level;
        this.money = money;
        this.points = points;
        this.ranksIds = ranksIds;
        this.firstConnection = firstConnection;
        this.lastConnection = lastConnection;
        this.lastDisconnection = lastDisconnection;
        this.superUser = superUser;
    }
}
