package net.valneas.account.rank;

import dev.morphia.Datastore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;

import java.util.ArrayList;
import java.util.List;

public class RankHandler extends AbstractRankHandler<RankUnit> {

    public RankHandler(Datastore datastore) {
        super(datastore, RankUnit.class);
    }

    public Component getRankList(){
        List<Component> ranks = new ArrayList<>();

        for (AbstractRankUnit next : getAllRanksQuery()) {
            if (next instanceof RankUnit rank) {
                ranks.add(rank.name().color(rank.getColor()));
            }
        }

        return Component.join(JoinConfiguration.commas(true), ranks);
    }
}
