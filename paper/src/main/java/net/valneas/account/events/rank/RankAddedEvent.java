package net.valneas.account.events.rank;

import net.valneas.account.AbstractAccountManager;
import net.valneas.account.events.AccountEvent;
import net.valneas.account.rank.RankUnit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Nullable;

public class RankAddedEvent extends AccountEvent {

    private static final HandlerList handlerList = new HandlerList();
    private final RankUnit rankAdded;
    private final CommandSender sender;

    public RankAddedEvent(AbstractAccountManager account, RankUnit rankAdded, @Nullable CommandSender sender) {
        super(account);
        this.rankAdded = rankAdded;
        this.sender = sender;
    }

    public RankUnit getRankAdded() {
        return rankAdded;
    }

    public @Nullable CommandSender getSender() {
        return sender;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
