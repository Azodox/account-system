package net.valneas.account.permission;

public class PermissionDispatcher {

    private final AccountSystem accountSystem;

    public PermissionDispatcher(AccountSystem accountSystem){
        this.accountSystem = accountSystem;
    }

    public void onEnable(){
        Bukkit.getOnlinePlayers().forEach(player -> {
            var account = new AccountManager(this.accountSystem, player);
            var rank = account.newRankManager();

            //TODO : get player's rank's permissions and add them to the player.
        })
    }
}