package net.valneas.account.events.rank;

import net.valneas.account.AbstractAccountManager;
import net.valneas.account.events.AccountEvent;
import net.valneas.account.rank.RankUnit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Nullable;

public class MajorRankChangedEvent extends AccountEvent {

    private static final HandlerList handlerList = new HandlerList();
    private final RankUnit previousMajorRank;
    private final RankUnit newMajorRank;
    private final CommandSender sender;

    public MajorRankChangedEvent(AbstractAccountManager account, RankUnit previousMajorRank, RankUnit newMajorRank, @Nullable CommandSender sender) {
        super(account);
        this.previousMajorRank = previousMajorRank;
        this.newMajorRank = newMajorRank;
        this.sender = sender;
    }

    public RankUnit getPreviousMajorRank() {
        return previousMajorRank;
    }

    public RankUnit getNewMajorRank() {
        return newMajorRank;
    }

    public @Nullable CommandSender getSender() {
        return sender;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
