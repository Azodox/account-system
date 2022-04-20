package net.valneas.account.rank;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum RankUnit {

    ADMIN(1, 1, "Admin", TextColor.fromHexString("#FDF617") + "\u2730 " + NamedTextColor.DARK_RED + "Admin ", "", NamedTextColor.DARK_RED),
    HEAD(2, 2, "Head", "Head-", "", NamedTextColor.WHITE),
    DEVELOPPEUR(3, 3, "Développeur", "§c§oDéveloppeur ", "", NamedTextColor.RED),
    DEVELOPPEUSE(4, 4, "Développeuse", "§c§oDéveloppeuse ", "", NamedTextColor.RED),
    BUILDER(5, 5, "Builder", "§bBuilder ", "", NamedTextColor.AQUA),
    BUILDEUSE(6, 6, "Buildeuse", "§bBuildeuse ", "", NamedTextColor.AQUA),
    MODERATEUR(7, 7, "Modérateur", "§2Modérateur ", "", NamedTextColor.DARK_GREEN),
    MODERATRICE(8, 8, "Modératrice", "§2Modératrice ", "", NamedTextColor.DARK_GREEN),
    ASSISTANT(9, 9, "Assistant", "§aAssistant ", "", NamedTextColor.GREEN),
    ASSISTANTE(10, 10, "Assistante", "§aAssistante ", "", NamedTextColor.GREEN),
    STAFF(11, 15, "Staff", "§d\u2726§o Staff ", "", NamedTextColor.LIGHT_PURPLE),
    PARTENAIRE(12, 11, "Partenaire", "§6Partenaire ", "", NamedTextColor.GOLD),
    YOUTUBE(13, 12, "YouTube", "§c§nYouTube ", "", NamedTextColor.RED),
    TWITCH(14, 13, "Twitch", "§5§nTwitch ", "", NamedTextColor.DARK_PURPLE),
    JOUEUR(15, 14, "Joueur", "§7", "", NamedTextColor.GRAY);

    private final String name, prefix, suffix;
    private final int power, id;
    private final NamedTextColor color;

    RankUnit(int power, int id, String name, String prefix, String suffix, NamedTextColor color){
        this.power = power;
        this.id = id;
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public int getPower() {
        return power;
    }

    public int getId() {
        return id;
    }

    public NamedTextColor getColor() {
        return color;
    }

    public static RankUnit getByPower(int power){
        return Arrays.stream(RankUnit.values()).filter(r -> r.getPower() == power).findFirst().orElse(null);
    }

    public static RankUnit getById(int id){
        return Arrays.stream(RankUnit.values()).filter(r -> r.getId() == id).findFirst().orElse(null);
    }

    public static RankUnit getByName(String name){
        return Arrays.stream(RankUnit.values()).filter(r -> r.getName().equals(name)).findFirst().orElse(null);
    }

    public static RankUnit getByCommandArg(String arg){
        try{
            int i = Integer.parseInt(arg);
            return getById(i);
        } catch (Exception e){
            return getByName(arg);
        }
    }

    public static String getRankList(){
        List<String> ranks = new ArrayList<>();

        for (RankUnit rank : values()) {
            ranks.add(rank.getColor() + rank.getName());
        }

        return String.join("§f, ", ranks);
    }
}
