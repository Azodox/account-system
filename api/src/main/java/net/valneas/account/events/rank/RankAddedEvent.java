package net.valneas.account.events.rank;

import lombok.Getter;
import net.valneas.account.AbstractAccount;
import net.valneas.account.AccountManager;
import net.valneas.account.rank.RankManager;
import net.valneas.account.rank.RankUnit;


/**
 * @author Azodox_ (Luke)
 * 16/7/2022.
 * @param <T> the type of command sender that sent the command.
 */

public class RankAddedEvent<T> {

    private @Getter final AccountManager<? extends AbstractAccount, ? extends RankManager<? extends RankUnit>> accountManager;
    private @Getter final int rankId;
    private @Getter final T sender;

    public RankAddedEvent(AccountManager<? extends AbstractAccount, ? extends RankManager<? extends RankUnit>> accountManager, int rankId, T sender) {
        this.accountManager = accountManager;
        this.rankId = rankId;
        this.sender = sender;
    }
}
