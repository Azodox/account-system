package net.valneas.account.permission;

import lombok.Getter;
import net.valneas.account.rank.AbstractRankUnit;
import net.valneas.account.rank.RankHandler;
import net.valneas.account.rank.RankUnit;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Azodox_ (Luke)
 * 6/6/2022.
 */

public class Permission extends AbstractPermission {

    private transient final Set<RankUnit> rankUnits;
    private transient @Getter final Set<Object> exceptions;

    public Permission(String permission, boolean isDefault, @NotNull Set<UUID> playersUUIDs, @NotNull Set<RankUnit> rankUnits, @NotNull Set<Object> exceptions) {
        super(permission, isDefault, playersUUIDs.stream().map(UUID::toString).toList(), rankUnits.stream().map(AbstractRankUnit::getId).toList(), exceptions.stream().map(PermissionDatabase.DatabaseParser::parse).toList());
        this.rankUnits = rankUnits;
        this.exceptions = exceptions;
    }

    public Set<RankUnit> getRanks(RankHandler rankHandler) {
        if(rankUnits == null || rankUnits.isEmpty()) {
            return getRanksIds().stream().map(rankHandler::getById).map(query -> (RankUnit) query.first()).filter(Objects::nonNull).collect(Collectors.toSet());
        }
        return rankUnits;
    }
}
