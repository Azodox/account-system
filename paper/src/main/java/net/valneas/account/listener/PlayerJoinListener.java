package net.valneas.account.listener;

import net.valneas.account.AccountManager;
import net.valneas.account.AccountSystem;
import net.valneas.account.util.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author Azodox_ (Luke)
 * 15/5/2022.
 */

public class PlayerJoinListener implements Listener {

    private final AccountSystem main;

    public PlayerJoinListener(AccountSystem main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        var accountManager = new AccountManager(main, player);

        long current = System.currentTimeMillis();

        if(!accountManager.hasAnAccount()){
            accountManager.initDefaultAccount();
            accountManager.set("first-connection", current);
        }else{
            accountManager.updateOnLogin();
        }

        accountManager.set("last-connection", current);
        accountManager.set("last-ip", PlayerUtil.getIp(player));
        this.main.getPermissionDispatcher().set(player);
    }
}
