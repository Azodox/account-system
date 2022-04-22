package net.valneas.account.rank;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum RankUnit {

    ADMIN(1, 1, "Admin", Component.text("\u2730 ").color(TextColor.fromHexString("#FDF617")).append(Component.text("Admin ").color(NamedTextColor.DARK_RED)), Component.empty(), NamedTextColor.DARK_RED),
    HEAD(2, 2, "Head", Component.text("Head-"), Component.empty(), NamedTextColor.WHITE),
    DEVELOPPEUR(3, 3, "Développeur", Component.text("Développeur").color(NamedTextColor.RED).decorate(TextDecoration.ITALIC).append(Component.space()), Component.empty(), NamedTextColor.RED),
    DEVELOPPEUSE(4, 4, "Développeuse", Component.text("Développeuse").color(NamedTextColor.RED).decorate(TextDecoration.ITALIC).append(Component.space()), Component.empty(), NamedTextColor.RED),
    BUILDER(5, 5, "Builder", Component.text("Builder").color(NamedTextColor.AQUA).append(Component.space()), Component.empty(), NamedTextColor.AQUA),
    BUILDEUSE(6, 6, "Buildeuse", Component.text("Buildeuse").color(NamedTextColor.AQUA).append(Component.space()), Component.empty(), NamedTextColor.AQUA),
    MODERATEUR(7, 7, "Modérateur", Component.text("Modérateur").color(NamedTextColor.DARK_GREEN).append(Component.space()), Component.empty(), NamedTextColor.DARK_GREEN),
    MODERATRICE(8, 8, "Modératrice", Component.text("Modératrice").color(NamedTextColor.DARK_GREEN).append(Component.space()), Component.empty(), NamedTextColor.DARK_GREEN),
    ASSISTANT(9, 9, "Assistant", Component.text("Assistant").color(NamedTextColor.GREEN).append(Component.space()), Component.empty(), NamedTextColor.GREEN),
    ASSISTANTE(10, 10, "Assistante", Component.text("Assistante").color(NamedTextColor.GREEN).append(Component.space()), Component.empty(), NamedTextColor.GREEN),
    STAFF(11, 15, "Staff", Component.text("\u2726 Staff").color(NamedTextColor.LIGHT_PURPLE).decorate(TextDecoration.ITALIC).append(Component.space()), Component.empty(), NamedTextColor.LIGHT_PURPLE),
    PARTENAIRE(12, 11, "Partenaire", Component.text("Partenaire").color(NamedTextColor.GOLD).append(Component.space()), Component.empty(), NamedTextColor.GOLD),
    YOUTUBE(13, 12, "YouTube", Component.text("YouTube").color(NamedTextColor.RED).decorate(TextDecoration.UNDERLINED).append(Component.space()), Component.empty(), NamedTextColor.RED),
    TWITCH(14, 13, "Twitch", Component.text("Twitch").color(NamedTextColor.DARK_PURPLE).decorate(TextDecoration.UNDERLINED).append(Component.space()), Component.empty(), NamedTextColor.DARK_PURPLE),
    JOUEUR(15, 14, "Joueur", Component.text("").color(NamedTextColor.GRAY), Component.empty(), NamedTextColor.GRAY);

    private final String name;
    private final Component prefix, suffix;
    private final int power, id;
    private final NamedTextColor color;

    RankUnit(int power, int id, String name, Component prefix, Component suffix, NamedTextColor color){
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

    public Component getPrefix() {
        return prefix;
    }

    public Component getSuffix() {
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
