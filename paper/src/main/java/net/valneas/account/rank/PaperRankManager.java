package net.valneas.account.rank;

import com.google.common.base.Preconditions;
import com.mongodb.client.result.UpdateResult;
import net.valneas.account.PaperAccountManager;
import net.valneas.account.events.rank.MajorRankChangedEvent;
import net.valneas.account.events.rank.RankAddedEvent;
import net.valneas.account.events.rank.RankRemovedEvent;
import org.bukkit.command.CommandSender;
import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Objects;

/**
 * @author Azodox_ (Luke)
 * 5/6/2022.
 */

public class PaperRankManager extends AbstractRankManager<PaperAccountManager, PaperRankUnit> {

    private final PaperRankHandler rankHandler;
    private final PaperAccountManager accountManager;

    public PaperRankManager(RankHandler<PaperRankUnit> rankHandler, PaperAccountManager accountManager) {
        super(accountManager);
        if(rankHandler instanceof PaperRankHandler handler){
            this.rankHandler = handler;
        }else{
            throw new IllegalArgumentException("rankHandler must be of type PaperRankHandler");
        }

        this.accountManager = accountManager;
    }

    /**
     * Set the major rank of the account. This method allows you to choose whether to call any events.
     * @param rankId The id of the rank to set as the major rank.
     * @param sender The sender of the command. (might be null)
     * @param event Whether to call the events.
     * @return The result of the update.
     */
    public <T> UpdateResult setMajorRank(int rankId, T sender,  boolean event){
        Preconditions.checkNotNull(sender, "CommandSender cannot be null");

        PaperRankUnit previousMajorRank = this.getMajorRank();
        UpdateResult result = this.setMajorRank(rankId);
        if(event && sender instanceof CommandSender commandSender) {
            EventBus.getDefault().post(new MajorRankChangedEvent<>(this.accountManager, previousMajorRank.getId(), rankId, commandSender));
        }
        return result;
    }

    /**
     * Add a rank to the account. This method allows you to choose whether to call any events.
     * @param rankId The id of the rank to add.
     * @param sender The command sender who executed the command. (might be null)
     * @param event Whether to call the event.
     * @return The result of the update.
     */
    public <T> UpdateResult addRank(int rankId, T sender, boolean event){
        Preconditions.checkNotNull(sender, "CommandSender cannot be null.");

        UpdateResult result = super.addRank(rankId);
        if(event && sender instanceof CommandSender commandSender){
            EventBus.getDefault().post(new RankAddedEvent<>(this.accountManager, rankId, commandSender));
        }
        return result;
    }

    /**
     * Remove a rank from the account. This method allows you to choose whether to call any events.
     * @param rankId The id of the rank to remove.
     * @param sender The command sender who executed the command. (might be null)
     * @param event Whether to call the event.
     * @return The result of the update.
     */
    public <T> UpdateResult removeRank(int rankId, T sender,  boolean event){
        Preconditions.checkNotNull(sender, "CommandSender cannot be null.");

        UpdateResult result = this.removeRank(rankId);
        if(event && sender instanceof CommandSender commandSender){
            EventBus.getDefault().post(new RankRemovedEvent<>(this.accountManager, rankId, commandSender));
        }
        return result;
    }

    @Override
    public boolean hasRank(int rankPower) {
        var majorRank = Preconditions.checkNotNull(rankHandler.getById(accountManager.getAccount().getMajorRankId()).first(), "Major rank not found");
        if(majorRank.getPower() == rankPower){
            return true;
        } else {
            if (getRanks().stream().anyMatch(unit -> unit.getPower() == rankPower)){
                return true;
            } else {
                return hasAtLeast(rankPower);
            }
        }
    }

    @Override
    public boolean hasAtLeast(int rankPower) {
        var majorRank = rankHandler.getById(accountManager.getAccount().getMajorRankId()).first();
        Preconditions.checkNotNull(majorRank, "Major rank not found");
        if(majorRank.getPower() <= rankPower){
            return true;
        } else {
            return getRanks().stream().anyMatch(unit -> unit.getPower() <= rankPower);
        }
    }

    @Override
    public PaperRankUnit getMajorRank() {
        var rank = rankHandler.getById(accountManager.getAccount().getMajorRankId()).first();
        Preconditions.checkNotNull(rank, "Major rank not found");

        return rank;
    }

    @Override
    public List<PaperRankUnit> getRanks() {
        return accountManager.getAccount().getRanksIds().stream().map(id -> rankHandler.getById(id).first()).filter(Objects::nonNull).toList();
    }
}
