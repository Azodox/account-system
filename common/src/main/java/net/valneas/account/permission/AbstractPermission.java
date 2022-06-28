package net.valneas.account.permission;

import dev.morphia.annotations.*;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Azodox_ (Luke)
 * 6/6/2022.
 */

@Entity(value = "permissions", discriminator = "permission")
@Indexes({
        @Index(fields = @Field("permission"), options = @IndexOptions(unique = true)),
        @Index(fields = @Field("default")),
        @Index(fields = @Field("players")),
        @Index(fields = @Field("ranks")),
        @Index(fields = @Field("except"))
})
public abstract class AbstractPermission {

    @Id
    private ObjectId _id;
    @Property
    private @Getter final String permission;
    @Property
    private @Getter final boolean isDefault;
    @Property("players")
    private @Getter final List<String> playersIds;
    @Property("ranks")
    private @Getter final List<Integer> ranksIds;
    @Property("except")
    private @Getter final List<String> exceptIds;

    public AbstractPermission(String permission, boolean isDefault, List<String> playersIds, List<Integer> ranksIds, List<String> exceptIds) {
        this.permission = permission;
        this.isDefault = isDefault;
        this.playersIds = playersIds;
        this.ranksIds = ranksIds;
        this.exceptIds = exceptIds;
    }

    public Set<UUID> getPlayers(){
        return playersIds.stream().map(UUID::fromString).collect(Collectors.toSet());
    }
}
