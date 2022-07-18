package net.valneas.account.listener;

import io.github.leonardosnt.bungeechannelapi.BungeeChannelApi;
import net.valneas.account.PaperAccountSystem;
import net.valneas.account.events.rank.RankAddedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.greenrobot.eventbus.Subscribe;

public class RankAddedListener {

    private final PaperAccountSystem main;

    public RankAddedListener(PaperAccountSystem main) {
        this.main = main;
    }

    @Subscribe
    public void onRankAdded(RankAddedEvent<CommandSender> event) {
        final var account = event.getAccountManager();

        String message = ChatColor.YELLOW + "" + ChatColor.ITALIC + "Michel §r§l➔ " + ChatColor.YELLOW +
                "Youpiii on vient de t'ajouter le rang §r" + ChatColor.BOLD + main.getRankHandler().getById(event.getRankId()).first().getName() + ChatColor.YELLOW +
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

        event.getSender().sendMessage(
                ChatColor.YELLOW + "" + ChatColor.ITALIC + "Michel §r§l➔ " + ChatColor.YELLOW +
                        "Message transmit et rang changé ✔"
        );
    }
}
