package net.valneas.account.listener;

import dev.morphia.query.experimental.updates.UpdateOperators;
import net.valneas.account.PaperAccountManager;
import net.valneas.account.PaperAccountSystem;
import net.valneas.account.util.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final PaperAccountSystem main;

    public PlayerQuitListener(PaperAccountSystem main) {
        this.main = main;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        PaperAccountManager accountManager = new PaperAccountManager(main, player);

        if(accountManager.hasAnAccount()){
            accountManager.getAccountQuery().update(UpdateOperators.set("last-disconnection", System.currentTimeMillis())).execute();
            accountManager.getAccountQuery().update(UpdateOperators.set("last-ip", PlayerUtil.getIp(player))).execute();
        }
    }
}
