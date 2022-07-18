package net.valneas.account;

import dev.morphia.Datastore;
import net.valneas.account.permission.PermissionDispatcher;
import net.valneas.account.rank.RankHandler;
import net.valneas.account.rank.RankManager;
import net.valneas.account.rank.RankUnit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Azodox_ (Luke)
 * 14/7/2022.
 */

public interface AccountSystemApi {

    @NotNull <T extends RankUnit> RankHandler<T> getRankHandler();
    @NotNull <A extends AbstractAccount, R extends RankManager<? extends RankUnit>, T> AccountManager<A, R> getAccountManager(T playerType);
    @Nullable
    PermissionDispatcher getPermissionDispatcher();
    Datastore getDatastore();
    void registerAccountSystemService();
    void registerRankHandlerService();
}
