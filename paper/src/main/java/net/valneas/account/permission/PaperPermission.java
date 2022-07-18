package net.valneas.account.permission;

import dev.morphia.query.Query;
import lombok.Getter;
import net.valneas.account.rank.AbstractRankUnit;
import net.valneas.account.rank.PaperRankHandler;
import net.valneas.account.rank.PaperRankUnit;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Azodox_ (Luke)
 * 6/6/2022.
 */

public class PaperPermission extends AbstractPermission {

    private transient final Set<PaperRankUnit> rankUnits;
    private transient @Getter final Set<Object> exceptions;

    public PaperPermission(String permission, boolean isDefault, @NotNull Set<UUID> playersUUIDs, @NotNull Set<PaperRankUnit> rankUnits, @NotNull Set<Object> exceptions) {
        super(permission, isDefault, playersUUIDs.stream().map(UUID::toString).toList(), rankUnits.stream().map(AbstractRankUnit::getId).toList(), exceptions.stream().map(PaperPermissionDatabase.DatabaseParser::parse).toList());
        this.rankUnits = rankUnits;
        this.exceptions = exceptions;
    }

    public Set<PaperRankUnit> getRanks(PaperRankHandler rankHandler) {
        if(rankUnits == null || rankUnits.isEmpty()) {
            return getRanksIds().stream().map(rankHandler::getById).map(Query::first).filter(Objects::nonNull).collect(Collectors.toSet());
        }
        return rankUnits;
    }
}
