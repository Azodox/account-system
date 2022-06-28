package net.valneas.account.rank;

import com.google.common.base.Preconditions;
import com.mongodb.client.result.UpdateResult;
import net.valneas.account.AccountManager;
import net.valneas.account.events.rank.MajorRankChangedEvent;
import net.valneas.account.events.rank.RankAddedEvent;
import net.valneas.account.events.rank.RankRemovedEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Objects;

/**
 * @author Azodox_ (Luke)
 * 5/6/2022.
 */

public class RankManager extends AbstractRankManager<RankUnit> {

    private final RankHandler rankHandler;
    private final AccountManager accountManager;

    public RankManager(RankHandler rankHandler, AccountManager accountManager) {
        super(accountManager);
        this.rankHandler = rankHandler;
        this.accountManager = accountManager;
    }

    /**
     * Set the major rank of the account. This method allows you to choose whether to call any events.
     * @param rankUnit The rank unit to set as the major rank.
     * @param sender The sender of the command. (might be null)
     * @param event Whether to call the events.
     * @return The result of the update.
     */
    public UpdateResult setMajorRank(RankUnit rankUnit, CommandSender sender,  boolean event){
        Preconditions.checkNotNull(rankUnit, "RankUnit cannot be null");
        Preconditions.checkNotNull(sender, "CommandSender cannot be null");

        RankUnit previousMajorRank = (RankUnit) this.getMajorRank();
        UpdateResult result = this.setMajorRank(rankUnit.getId());
        if(event) {
            Bukkit.getPluginManager().callEvent(new MajorRankChangedEvent(accountManager, previousMajorRank, rankUnit, sender));
        }
        return result;
    }

    /**
     * Add a rank to the account. This method allows you to choose whether to call any events.
     * @param rankUnit The rank to add.
     * @param sender The command sender who executed the command. (might be null)
     * @param event Whether to call the event.
     * @return The result of the update.
     */
    public UpdateResult addRank(RankUnit rankUnit, CommandSender sender, boolean event){
        Preconditions.checkNotNull(rankUnit, "RankUnit cannot be null.");
        Preconditions.checkNotNull(sender, "CommandSender cannot be null.");

        UpdateResult result = super.addRank(rankUnit.getId());
        if(event){
            Bukkit.getPluginManager().callEvent(new RankAddedEvent(accountManager, rankUnit, sender));
        }
        return result;
    }

    /**
     * Remove a rank from the account. This method allows you to choose whether to call any events.
     * @param rankUnit The rank to remove.
     * @param sender The command sender who executed the command. (might be null)
     * @param event Whether to call the event.
     * @return The result of the update.
     */
    public UpdateResult removeRank(RankUnit rankUnit, CommandSender sender,  boolean event){
        Preconditions.checkNotNull(rankUnit, "RankUnit cannot be null.");
        Preconditions.checkNotNull(sender, "CommandSender cannot be null.");

        UpdateResult result = this.removeRank(rankUnit.getId());
        if(event){
            Bukkit.getPluginManager().callEvent(new RankRemovedEvent(accountManager, rankUnit, sender));
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
    public RankUnit getMajorRank() {
        var rank = rankHandler.getById(accountManager.getAccount().getMajorRankId()).first();
        Preconditions.checkNotNull(rank, "Major rank not found");

        return rank;
    }

    @Override
    public List<RankUnit> getRanks() {
        return accountManager.getAccount().getRanksIds().stream().map(id -> rankHandler.getById(id).first()).filter(Objects::nonNull).toList();
    }
}
