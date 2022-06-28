package net.valneas.account.rank;

import com.google.common.base.Preconditions;
import net.valneas.account.VelocityAccountManager;

import java.util.List;
import java.util.Objects;

/**
 * @author Azodox_ (Luke)
 * 19/6/2022.
 */

public class VelocityRankManager extends AbstractRankManager<VelocityRankUnit> {

    private final VelocityRankHandler rankHandler;

    public VelocityRankManager(VelocityRankHandler rankHandler, VelocityAccountManager accountManager) {
        super(accountManager);
        this.rankHandler = rankHandler;
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
        var majorRank = Preconditions.checkNotNull(rankHandler.getById(accountManager.getAccount().getMajorRankId()).first(), "Major rank not found");
        if(majorRank.getPower() <= rankPower){
            return true;
        } else {
            return getRanks().stream().anyMatch(unit -> unit.getPower() <= rankPower);
        }
    }

    @Override
    public VelocityRankUnit getMajorRank() {
        return Preconditions.checkNotNull(rankHandler.getById(accountManager.getAccount().getMajorRankId()).first(), "Major rank not found");
    }

    @Override
    public List<VelocityRankUnit> getRanks() {
        return accountManager.getAccount().getRanksIds().stream().map(id -> rankHandler.getById(id).first()).filter(Objects::nonNull).toList();
    }
}
