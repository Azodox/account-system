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
        @Index(fields = @Field("uuid"), options = @IndexOptions(unique = true)),
        @Index(fields = @Field("xp")),
        @Index(fields = @Field("level")),
        @Index(fields = @Field("money")),
        @Index(fields = @Field("major-rank")),
        @Index(fields = @Field("ranks")),
        @Index(fields = @Field("moderation-mod")),
        @Index(fields = @Field("last-ip")),
        @Index(fields = @Field("last-connection")),
        @Index(fields = @Field("last-disconnection")),
        @Index(fields = @Field("first-connection")),
        @Index(fields = @Field("farming-points")),
        @Index(fields = @Field("farming-prestige")),
        @Index(fields = @Field("points")),
        @Index(fields = @Field("super-user"))
})
public abstract class AbstractAccount {

    @Id
    private ObjectId _id;

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

    public AbstractAccount(String uuid, String name, String lastIp, int majorRankId, boolean moderationMode, boolean vanish, double farmingPoints, double farmingPrestige, double xp, double level, double money, double points, List<Integer> ranksIds, long firstConnection, long lastConnection, long lastDisconnection, boolean superUser) {
        this.uuid = uuid;
        this.name = name;
        this.lastIp = lastIp;
        this.majorRankId = majorRankId;
        this.moderationMode = moderationMode;
        this.vanish = vanish;
        this.farmingPoints = farmingPoints;
        this.farmingPrestige = farmingPrestige;
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
