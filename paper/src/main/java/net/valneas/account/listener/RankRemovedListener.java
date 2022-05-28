package net.valneas.account.listener;

import io.github.leonardosnt.bungeechannelapi.BungeeChannelApi;
import net.valneas.account.AccountSystem;
import net.valneas.account.events.rank.RankRemovedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RankRemovedListener implements Listener {

    private final AccountSystem main;

    public RankRemovedListener(AccountSystem main) {
        this.main = main;
    }

    @EventHandler
    public void onRankRemoved(RankRemovedEvent e){
        final var account = e.getAccount();

        String message = ChatColor.YELLOW + "" + ChatColor.ITALIC + "Michel §r§l➔ " + ChatColor.YELLOW +
                "Coucou, apparement tu as perdu le rang §r" + ChatColor.BOLD + e.getRankRemoved().getName() + ChatColor.YELLOW +
                ". Je ne ressens pas d'émotion mais si c'était important, je suis désolé pour toi. #SAD";

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
