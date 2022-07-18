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

public class MajorRankChangedEvent<T> {

    private @Getter final AccountManager<? extends AbstractAccount, ? extends RankManager<? extends RankUnit>> accountManager;
    private @Getter final int previousRankId;
    private @Getter final int newRankId;
    private @Getter final T sender;

    public MajorRankChangedEvent(AccountManager<? extends AbstractAccount, ? extends RankManager<? extends RankUnit>> accountManager, int previousRankId, int newRankId, T sender) {
        this.accountManager = accountManager;
        this.previousRankId = previousRankId;
        this.newRankId = newRankId;
        this.sender = sender;
    }
}
