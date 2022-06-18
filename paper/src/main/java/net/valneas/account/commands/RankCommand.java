package net.valneas.account.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.valneas.account.AccountManager;
import net.valneas.account.AccountSystem;
import net.valneas.account.rank.RankUnit;
import net.valneas.account.util.PlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The rank command class.
 * Add, remove, see, the ranks of a player.
 * You can also type /rank list to see the list of the ranks.
 */
public class RankCommand implements CommandExecutor {

    private final AccountSystem main;

    public RankCommand(AccountSystem main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        /*
         * If the sender is a player, check if he has the permission
         * to execute this command.
         */
        if(sender instanceof Player player){
            final var account = new AccountManager(main, player);
            final var rank = account.newRankManager();
            if(!account.getAccount().isSuperUser()){
                player.sendMessage(ChatColor.RED + "Erreur : Vous n'avez pas la permission.");
                return true;
            }
        }

        /*
         * When there's none args.
         */
        if (args.length == 0) {
            sender.sendMessage(getUsageMessage());
            return true;
        }

        /*
         * The show arg.
         * Used to see the ranks of a player.
         */
        if (args[0].equalsIgnoreCase("show")) {
            if (args.length > 1) {
                /*
                 * More than 2 args = Send the usage message to the sender.
                 */
                if (args.length >= 3) {
                    sender.sendMessage(getUsageMessage());
                    return true;
                }

                /*
                 * Trying to get the account of the potential player put in the first arg.
                 */
                final AccountManager account;

                if (PlayerUtil.ArgIsAnUuid(args[1])) {
                    if (AccountManager.existsByUUID(args[1])) {
                        account = new AccountManager(main, AccountManager.getNameByUuid(args[1]), args[1]);
                    } else {
                        sender.sendMessage(ChatColor.RED + "Erreur : Ce compte n'existe pas.");
                        return true;
                    }
                } else {
                    if (AccountManager.existsByName(args[1])) {
                        account = new AccountManager(main, args[1], AccountManager.getUuidByName(args[1]));
                    } else {
                        sender.sendMessage(ChatColor.RED + "Erreur : Ce compte n'existe pas.");
                        return true;
                    }
                }

                /*
                 * If account exists then show the ranks.
                 */
                if (account.hasAnAccount()) {
                    final var rank = account.newRankManager();

                    /*
                     * If the player hasn't any rank.
                     */
                    if (!rank.hasMajorRank() && !rank.hasRanks()) {
                        sender.sendMessage(ChatColor.YELLOW + "Michel §f➤" + ChatColor.YELLOW + " Cette personne n'a aucun rang.");
                        return true;
                    }

                    /*
                     * Michel message builder.
                     */
                    StringBuilder sb = new StringBuilder();
                    sb.append(ChatColor.YELLOW + "Michel §f➤" + ChatColor.YELLOW + "" + ChatColor.ITALIC + " Les rangs de §r" + ChatColor.WHITE + ""
                            + ChatColor.BOLD + ChatColor.UNDERLINE + account.getName() + "§r" +
                            ChatColor.YELLOW + "§o sont :\n");
                    /*
                     * Add the major rank in the message builder.
                     */
                    if (rank.hasMajorRank()) {
                        sb.append("§f- " + ChatColor.GRAY + rank.getMajorRank().getName() + ChatColor.DARK_GRAY + " (" + ChatColor.GOLD + "Id §f: " + ChatColor.GOLD +
                                rank.getMajorRank().getId() + ChatColor.DARK_GRAY + ") "
                                + ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "Rang majeur" + ChatColor.DARK_GREEN + "]\n");
                    }

                    /*
                     * Add the ranks in the message builder.
                     */
                    if (rank.hasRanks()) {
                        for (RankUnit r : rank.getRanks().stream().map(r -> (RankUnit) r).toList()) {
                            sb.append("§f- " + ChatColor.GRAY + r.getName() + ChatColor.DARK_GRAY + " (" + ChatColor.GOLD + "Id §f: " + ChatColor.GOLD +
                                    r.getId() + ChatColor.DARK_GRAY + ")\n");
                        }
                    }

                    /*
                     * Send it to the sender.
                     */
                    sender.sendMessage(sb.toString());
                }
            } else {
                /*
                 * If less than 2 args = Send usage message.
                 */
                sender.sendMessage(getUsageMessage());
            }
            /*
             * The add arg.
             * Used to add a rank (not major rank) to a player.
             */
        } else if (args[0].equalsIgnoreCase("add")) {
            if (args.length > 1) {
                if (args.length > 2) {
                    /*
                     * More than 3 args = Send usage message.
                     */
                    if (args.length > 3) {
                        sender.sendMessage(getUsageMessage());
                        return true;
                    }

                    /*
                     * Trying to get the account of the potential player put in the first arg.
                     */
                    final AccountManager account;

                    if (PlayerUtil.ArgIsAnUuid(args[1])) {
                        if (AccountManager.existsByUUID(args[1])) {
                            account = new AccountManager(main, AccountManager.getNameByUuid(args[1]), args[1]);
                        } else {
                            sender.sendMessage(ChatColor.RED + "Erreur : Ce compte n'existe pas.");
                            return true;
                        }
                    } else {
                        if (AccountManager.existsByName(args[1])) {
                            account = new AccountManager(main, args[1], AccountManager.getUuidByName(args[1]));
                        } else {
                            sender.sendMessage(ChatColor.RED + "Erreur : Ce compte n'existe pas.");
                            return true;
                        }
                    }

                    /*
                     * If account exists then check and add the rank.
                     */
                    if (account.hasAnAccount()) {
                        final var rank = account.newRankManager();

                        /*
                         * If the rank is null.
                         */
                        final RankUnit rankUnit = (RankUnit) main.getRankHandler().getByCommandArg(args[2]).first();
                        if (rankUnit == null) {
                            sender.sendMessage(Component.text("Le rang ne peut pas etre null !\nVoici la liste des rangs :\n").append(main.getRankHandler().getRankList()));
                            return true;
                        }

                        /*
                         * If the player has already the rank.
                         */
                        if (rank.hasExactRank(rankUnit.getId())) {
                            sender.sendMessage(ChatColor.RED + "Erreur : Cette personne a déjà le rang.");
                            return true;
                        }

                        /*
                         * Add the rank to the player
                         * and call the event.
                         */
                        rank.addRank(rankUnit, sender, true);
                    } else {
                        /*
                         * If the account doesn't exists.
                         */
                        sender.sendMessage(ChatColor.RED + "Erreur : Ce compte n'existe pas.");
                        return true;
                    }
                } else {
                    /*
                     * If less than 2 args = Send usage message.
                     */
                    sender.sendMessage(getUsageMessage());
                }
            } else {
                /*
                 * If less than 1 arg = Send usage message.
                 */
                sender.sendMessage(getUsageMessage());
            }
            /*
             * The remove arg.
             * Used to remove a rank of a player.
             */
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length > 1) {
                if (args.length > 2) {
                    if (args.length > 3) {
                        sender.sendMessage(getUsageMessage());
                        return true;
                    }

                    final AccountManager account;

                    if (PlayerUtil.ArgIsAnUuid(args[1])) {
                        if (AccountManager.existsByUUID(args[1])) {
                            account = new AccountManager(main, AccountManager.getNameByUuid(args[1]), args[1]);
                        } else {
                            sender.sendMessage(ChatColor.RED + "Erreur : Ce compte n'existe pas.");
                            return true;
                        }
                    } else {
                        if (AccountManager.existsByName(args[1])) {
                            account = new AccountManager(main, args[1], AccountManager.getUuidByName(args[1]));
                        } else {
                            sender.sendMessage(ChatColor.RED + "Erreur : Ce compte n'existe pas.");
                            return true;
                        }
                    }

                    if (account.hasAnAccount()) {
                        final var rank = account.newRankManager();

                        final RankUnit rankUnit = (RankUnit) main.getRankHandler().getByCommandArg(args[2]).first();
                        if (rankUnit == null) {
                            sender.sendMessage(Component.text("Le rang ne peut pas etre null !\nVoici la liste des rangs :\n").append(main.getRankHandler().getRankList()));
                            return true;
                        }

                        if (!rank.hasMajorRank() || !rank.hasRanks() || !rank.hasExactMajorOrNotRank(rankUnit.getId())) {
                            sender.sendMessage(ChatColor.RED + "Erreur : Cette personne n'a pas le rang.");
                            return true;
                        }

                        rank.removeRank(rankUnit, sender, true);
                    } else {
                        sender.sendMessage(ChatColor.RED + "Erreur : Ce compte n'existe pas.");
                        return true;
                    }
                } else {
                    sender.sendMessage(getUsageMessage());
                }
            } else {
                sender.sendMessage(getUsageMessage());
            }
        } else if (args[0].equalsIgnoreCase("set")) {
            if (args.length > 1) {
                if (args.length > 2) {
                    if (args.length > 3) {
                        sender.sendMessage(getUsageMessage());
                        return true;
                    }

                    final AccountManager account;

                    if (PlayerUtil.ArgIsAnUuid(args[1])) {
                        if (AccountManager.existsByUUID(args[1])) {
                            account = new AccountManager(main, AccountManager.getNameByUuid(args[1]), args[1]);
                        } else {
                            sender.sendMessage(ChatColor.RED + "Erreur : Ce compte n'existe pas.");
                            return true;
                        }
                    } else {
                        if (AccountManager.existsByName(args[1])) {
                            account = new AccountManager(main, args[1], AccountManager.getUuidByName(args[1]));
                        } else {
                            sender.sendMessage(ChatColor.RED + "Erreur : Ce compte n'existe pas.");
                            return true;
                        }
                    }

                    if (account.hasAnAccount()) {
                        final var rank = account.newRankManager();

                        final RankUnit rankUnit = (RankUnit) main.getRankHandler().getByCommandArg(args[2]).first();
                        if (rankUnit == null) {
                            sender.sendMessage(Component.text("Le rang ne peut pas etre null !\nVoici la liste des rangs :\n").append(main.getRankHandler().getRankList()));
                            return true;
                        }

                        if (rank.hasExactMajorRank(rankUnit.getId())) {
                            sender.sendMessage(ChatColor.RED + "Erreur : Cette personne a déjà le rang.");
                            return true;
                        }

                        if(rankUnit.getId() != main.getRankHandler().getDefaultRank().getId() && !rank.hasExactRank(main.getRankHandler().getDefaultRank().getId())) {
                            rank.addRank(main.getRankHandler().getDefaultRank().getId());
                        }

                        if(rankUnit.getId() == main.getRankHandler().getDefaultRank().getId() && rank.hasExactRank(main.getRankHandler().getDefaultRank().getId())) {
                            rank.removeRank(main.getRankHandler().getDefaultRank().getId());
                        }

                        rank.setMajorRank(rankUnit, sender, true);
                    } else {
                        sender.sendMessage(ChatColor.RED + "Erreur : Ce compte n'existe pas.");
                        return true;
                    }
                } else {
                    sender.sendMessage(getUsageMessage());
                }
            } else {
                sender.sendMessage(getUsageMessage());
            }
        } else if(args[0].equalsIgnoreCase("list")){
            if(args.length > 1){
                sender.sendMessage(getUsageMessage());
                return true;
            }

            sender.sendMessage(
                    Component.text("                                                \n").color(NamedTextColor.DARK_GRAY).decorate(TextDecoration.STRIKETHROUGH)
                            .append(main.getRankHandler().getRankList()).style(Style.empty())
                            .append(Component.text("\n                                                ").color(NamedTextColor.DARK_GRAY).decorate(TextDecoration.STRIKETHROUGH)));
        }

        return false;
    }

    private String getUsageMessage() {
        return "/rank [add/remove/set/show] [joueur] [rang]";
    }
}
