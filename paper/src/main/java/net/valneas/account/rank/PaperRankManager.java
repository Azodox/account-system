package net.valneas.account.rank;

import com.google.common.base.Preconditions;
import net.valneas.account.PaperAccountManager;
import net.valneas.account.events.rank.MajorRankChangedEvent;
import net.valneas.account.events.rank.RankAddedEvent;
import net.valneas.account.events.rank.RankRemovedEvent;
import org.bukkit.command.CommandSender;
import org.greenrobot.eventbus.EventBus;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Objects;

/**
 * @author Azodox_ (Luke)
 * 5/6/2022.
 */

public class PaperRankManager extends AbstractRankManager<PaperAccountManager, PaperRankUnit> {

    private final PaperRankHandler rankHandler;

    public PaperRankManager(JedisPool jedis, RankHandler<PaperRankUnit> rankHandler, PaperAccountManager accountManager) {
        super(jedis, accountManager);
        if (rankHandler instanceof PaperRankHandler handler) {
            this.rankHandler = handler;
        } else {
            throw new IllegalArgumentException("rankHandler must be of type PaperRankHandler");
        }
    }

    /**
     * Set the major rank of the account. This method allows you to choose whether to call any events.
     *
     * @param rankId The id of the rank to set as the major rank.
     * @param sender The sender of the command. (might be null)
     * @param event  Whether to call the events.
     */
    public <T> void setMajorRank(int rankId, T sender, boolean event) {
        Preconditions.checkNotNull(sender, "CommandSender cannot be null");

        PaperRankUnit previousMajorRank = this.getMajorRank();
        if (event && sender instanceof CommandSender commandSender) {
            EventBus.getDefault().post(new MajorRankChangedEvent<>(this.accountManager, previousMajorRank.getId(), rankId, commandSender));
        }
    }

    /**
     * Add a rank to the account. This method allows you to choose whether to call any events.
     *
     * @param rankId The id of the rank to add.
     * @param sender The command sender who executed the command. (might be null)
     * @param event  Whether to call the event.
     */
    public <T> void addRank(int rankId, T sender, boolean event) {
        Preconditions.checkNotNull(sender, "CommandSender cannot be null.");

        super.addRank(rankId);
        if (event && sender instanceof CommandSender commandSender) {
            EventBus.getDefault().post(new RankAddedEvent<>(this.accountManager, rankId, commandSender));
        }
    }

    /**
     * Remove a rank from the account. This method allows you to choose whether to call any events.
     *
     * @param rankId The id of the rank to remove.
     * @param sender The command sender who executed the command. (might be null)
     * @param event  Whether to call the event.
     */
    public <T> void removeRank(int rankId, T sender, boolean event) {
        Preconditions.checkNotNull(sender, "CommandSender cannot be null.");

        this.removeRank(rankId);
        if (event && sender instanceof CommandSender commandSender) {
            EventBus.getDefault().post(new RankRemovedEvent<>(this.accountManager, rankId, commandSender));
        }
    }

    @Override
    public boolean hasRank(int rankPower) {
        var majorRank = this.getMajorRank();
        if (majorRank.getPower() == rankPower) {
            return true;
        } else {
            if (getRanks().stream().anyMatch(unit -> unit.getPower() == rankPower)) {
                return true;
            } else {
                return hasAtLeast(rankPower);
            }
        }
    }

    @Override
    public boolean hasAtLeast(int rankPower) {
        var majorRank = this.getMajorRank();
        if (majorRank.getPower() <= rankPower) {
            return true;
        } else {
            return getRanks().stream().anyMatch(unit -> unit.getPower() <= rankPower);
        }
    }

    @Override
    public PaperRankUnit getMajorRank() {
        try (var jedis = jedisPool.getResource()) {
            if (jedis.exists("account#" + accountManager.getAccount().getUuid()))
                return rankHandler.getById(Integer.parseInt(jedis.hget("account#" + accountManager.getAccount().getUuid(), "major-rank"))).first();
            else
                return rankHandler.getById(accountManager.getAccount().getMajorRankId()).first();
        }
    }

    @Override
    public List<PaperRankUnit> getRanks() {
        try (var jedis = jedisPool.getResource()) {
            var key = "account#" + accountManager.getAccount().getUuid();
            if (jedis.exists(key)) {
                return jedis.smembers(jedis.hget(key, "ranks")).stream().map(Integer::parseInt).map(id -> Preconditions.checkNotNull(rankHandler.getById(id).first())).toList();
            } else
                return accountManager.getAccount().getRanksIds().stream().map(id -> rankHandler.getById(id).first()).filter(Objects::nonNull).toList();
        }
    }
}
