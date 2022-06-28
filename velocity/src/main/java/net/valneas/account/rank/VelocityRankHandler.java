package net.valneas.account.rank;

import dev.morphia.Datastore;

/**
 * @author Azodox_ (Luke)
 * 19/6/2022.
 */

public class VelocityRankHandler extends AbstractRankHandler<VelocityRankUnit>{
    public VelocityRankHandler(Datastore datastore) {
        super(datastore, VelocityRankUnit.class);
    }
}
