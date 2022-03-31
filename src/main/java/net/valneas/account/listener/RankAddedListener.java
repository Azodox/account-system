package net.valneas.account.listener;

import io.github.leonardosnt.bungeechannelapi.BungeeChannelApi;
import net.valneas.account.AccountManager;
import net.valneas.account.AccountSystem;
import net.valneas.account.api.events.rank.RankAddedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RankAddedListener implements Listener {

    private final AccountSystem main;

    public RankAddedListener(AccountSystem main) {
        this.main = main;
    }

    @EventHandler
    public void onRankAdded(RankAddedEvent e){
        final AccountManager account = e.getAccount();

        String message = ChatColor.YELLOW + "" + ChatColor.ITALIC + "Michel §r§l➔ " + ChatColor.YELLOW +
                "Youpiii on vient de t'ajouter le rang §r" + ChatColor.BOLD + e.getRankAdded().getName() + ChatColor.YELLOW +
                "! Profitez-en bien";

        if(main.isBungeeMode()){
            final BungeeChannelApi api = BungeeChannelApi.of(main);
            api.getPlayerList("ALL")
                    .whenComplete((result, error) -> {
                        if(result.stream().anyMatch(s -> s.equals(account.getName()))){
                            api.sendMessage(account.getName(), message);
                        }
                    });
        }else{
            for (Player players : Bukkit.getOnlinePlayers()) {
                if(players.getName().equals(account.getName())){
                    players.sendMessage(message);
                }
            }
        }

        e.getSender().sendMessage(
                ChatColor.YELLOW + "" + ChatColor.ITALIC + "Michel §r§l➔ " + ChatColor.YELLOW +
                        "Message transmit et rang changé ✔"
        );
    }
}
