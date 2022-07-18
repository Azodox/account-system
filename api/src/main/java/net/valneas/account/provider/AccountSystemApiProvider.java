package net.valneas.account.provider;

//Totally "inspired" by LuckPerms, yes.

import lombok.Getter;
import net.valneas.account.AccountSystemApi;
import net.valneas.account.rank.RankHandler;

/**
 * @author Azodox_ (Luke)
 * 14/7/2022.
 */

public final class AccountSystemApiProvider {

    private @Getter static AccountSystemApi api;
    private @Getter static RankHandler<?> rankHandler;

    public static void register(AccountSystemApi api) {
        AccountSystemApiProvider.api = api;
    }
    public static void register(RankHandler<?> rankHandler) {
        AccountSystemApiProvider.rankHandler = rankHandler;
    }

    public static void unregister() {
        AccountSystemApiProvider.api = null;
        AccountSystemApiProvider.rankHandler = null;
    }

    private AccountSystemApiProvider() {
        throw new IllegalStateException("Utility class, cannot be instantiated");
    }
}
