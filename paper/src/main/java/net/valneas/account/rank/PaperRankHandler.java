package net.valneas.account.rank;

import dev.morphia.Datastore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;

import java.util.ArrayList;
import java.util.List;

public class PaperRankHandler extends AbstractRankHandler<PaperRankUnit> {

    public PaperRankHandler(Datastore datastore) {
        super(datastore, PaperRankUnit.class);
    }

    public Component getRankList(){
        List<Component> ranks = new ArrayList<>();

        for (PaperRankUnit rank : getAllRanksQuery()) {
            ranks.add(rank.name().color(rank.color()));
        }

        return Component.join(JoinConfiguration.commas(true), ranks);
    }
}
