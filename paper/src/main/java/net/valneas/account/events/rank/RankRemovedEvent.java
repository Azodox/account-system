package net.valneas.account.events.rank;

import net.valneas.account.AbstractAccountManager;
import net.valneas.account.events.AccountEvent;
import net.valneas.account.rank.RankUnit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

public class RankRemovedEvent extends AccountEvent {

    private static final HandlerList handlerList = new HandlerList();
    private final RankUnit rankRemoved;
    private final CommandSender sender;

    public RankRemovedEvent(AbstractAccountManager account, RankUnit rankRemoved, CommandSender sender) {
        super(account);
        this.rankRemoved = rankRemoved;
        this.sender = sender;
    }

    public RankUnit getRankRemoved() {
        return rankRemoved;
    }

    public CommandSender getSender() {
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
