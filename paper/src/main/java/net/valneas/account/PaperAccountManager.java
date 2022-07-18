package net.valneas.account;

import dev.morphia.query.experimental.filters.Filters;
import net.valneas.account.rank.PaperRankManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PaperAccountManager extends AbstractAccountManager<PaperRankManager> {

    private final PaperAccountSystem accountSystem;

    public PaperAccountManager(PaperAccountSystem accountSystem, Player player) {
        super(accountSystem.getDatastore(), player.getName(), player.getUniqueId().toString());
        this.accountSystem = accountSystem;
    }

    public PaperAccountManager(PaperAccountSystem accountSystem, String name, String uuid) {
        super(accountSystem.getDatastore(), name, uuid);
        this.accountSystem = accountSystem;
    }

    @Override
    public void createAccount(int defaultRankId) {
        if(hasAnAccount())
            return;

        var account = new Account(
                this.getUuid(),
                this.getName(),
                "",
                defaultRankId,
                false,
                false,
                0.0d,
                0.0d,
                0.0d,
                0.0d,
                0.0d,
                0.0d,
                new ArrayList<>(),
                0L,
                0L,
                0L,
                false
        );

        accountSystem.getDatastore().save(account);
    }

    public void initDefaultAccount(){
        this.createAccount(accountSystem.getRankHandler().getDefaultRank().getId());
    }

    @Override
    public PaperRankManager newRankManager() {
        return new PaperRankManager(accountSystem.getRankHandler(), this);
    }

    /*
    Static methods
     */

    public static boolean existsByUUID(String uuid){
        var provider = Bukkit.getServicesManager().getRegistration(AccountSystemApi.class);
        if(provider != null){
            return provider.getProvider().getDatastore().find(Account.class).filter(Filters.eq("uuid", uuid)).count() != 0;
        }
        return false;
    }

    public static boolean existsByName(String name){
        var provider = Bukkit.getServicesManager().getRegistration(AccountSystemApi.class);
        if(provider != null){
            return provider.getProvider().getDatastore().find(Account.class).filter(Filters.eq("name", name)).count() != 0;
        }
        return false;
    }

    public static String getUuidByName(String name){
        if(!existsByName(name)) return null;

        var provider = Bukkit.getServicesManager().getRegistration(AccountSystemApi.class);
        if(provider != null){
            var account = provider.getProvider().getDatastore().find(Account.class).filter(Filters.eq("name", name)).first();
            if(account != null)
                return account.getUuid();
        }
        return null;
    }

    public static String getNameByUuid(String uuid){
        if(!existsByUUID(uuid)) return null;

        var provider = Bukkit.getServicesManager().getRegistration(AccountSystemApi.class);
        if(provider != null){
            var account = provider.getProvider().getDatastore().find(Account.class).filter(Filters.eq("uuid", uuid)).first();
            if(account != null)
                return account.getName();
        }
        return null;
    }
}
