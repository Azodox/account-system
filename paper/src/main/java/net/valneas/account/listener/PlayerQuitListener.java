package net.valneas.account.listener;

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
        PaperAccountManager accountManager = new PaperAccountManager(main, player, main.getJedisPool());
        var jedis = main.getJedisPool().getResource();

        jedis.hset("account#" + player.getUniqueId(), "last-connection", String.valueOf(System.currentTimeMillis()));
        jedis.hset("account#" + player.getUniqueId(), "last-ip", PlayerUtil.getIp(player));

        jedis.close();
        main.getCacheSaver().saveInDB(player.getUniqueId());
    }
}
