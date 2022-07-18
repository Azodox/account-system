package net.valneas.account.listener;

import io.github.leonardosnt.bungeechannelapi.BungeeChannelApi;
import net.valneas.account.PaperAccountSystem;
import net.valneas.account.events.rank.RankRemovedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.greenrobot.eventbus.Subscribe;

public class RankRemovedListener {

    private final PaperAccountSystem main;

    public RankRemovedListener(PaperAccountSystem main) {
        this.main = main;
    }

    @Subscribe
    public void onRankRemoved(RankRemovedEvent<CommandSender> event) {
        final var account = event.getAccountManager();

        String message = ChatColor.YELLOW + "" + ChatColor.ITALIC + "Michel §r§l➔ " + ChatColor.YELLOW +
                "Coucou, apparement tu as perdu le rang §r" + ChatColor.BOLD + main.getRankHandler().getById(event.getRankId()).first().getName() + ChatColor.YELLOW +
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

        event.getSender().sendMessage(
                ChatColor.YELLOW + "" + ChatColor.ITALIC + "Michel §r§l➔ " + ChatColor.YELLOW +
                        "Message transmit et rang changé ✔"
        );
    }
}
