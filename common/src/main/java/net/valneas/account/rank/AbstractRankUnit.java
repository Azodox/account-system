package net.valneas.account.rank;

import dev.morphia.annotations.*;
import lombok.Getter;
import org.bson.types.ObjectId;

/**
 * @author Azodox_ (Luke)
 * 5/6/2022.
 */

@Entity(value = "rank", discriminator = "rank")
@Indexes({
        @Index(fields = @Field("name")),
        @Index(fields = @Field("prefix")),
        @Index(fields = @Field("suffix")),
        @Index(fields = @Field("color")),
        @Index(fields = @Field("id")),
        @Index(fields = @Field("power")),
        @Index(fields = @Field("default"))
})
public abstract class AbstractRankUnit implements RankUnit {

    @Id
    private ObjectId _id;
    @Property
    protected final String name, prefix, suffix, color;
    @Property
    protected @Getter final int power, id;
    @Property("default")
    protected @Getter final boolean isDefault;

    public AbstractRankUnit(String name, String prefix, String suffix, String color, int power, int id, boolean isDefault) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.color = color;
        this.power = power;
        this.id = id;
        this.isDefault = isDefault;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public String getSuffix() {
        return this.suffix;
    }

    @Override
    public String getColor() {
        return this.color;
    }
}
