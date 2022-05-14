package net.valneas.account.rank;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum RankUnit {

    ADMIN(1, 1, "Admin", Component.text("\u2730 ").color(TextColor.fromHexString("#FDF617")).append(Component.text("Admin ").color(NamedTextColor.DARK_RED)), Component.empty(), NamedTextColor.DARK_RED),
    HEAD(2, 2, "Head", Component.text("Head-"), Component.empty(), NamedTextColor.WHITE),
    DEVELOPPEMENT(3, 3, "Développement", Component.text("Développement").color(NamedTextColor.RED).decorate(TextDecoration.ITALIC).append(Component.space()), Component.empty(), NamedTextColor.RED),
    MODERATION(4, 7, "Modération", Component.text("Modération").color(NamedTextColor.DARK_GREEN).append(Component.space()), Component.empty(), NamedTextColor.DARK_GREEN),
    STAFF(5, 15, "Equipe", Component.text("\u2726 Équipe").color(NamedTextColor.LIGHT_PURPLE).decorate(TextDecoration.ITALIC).append(Component.space()), Component.empty(), NamedTextColor.LIGHT_PURPLE),
    PARTENAIRE(6, 11, "Partenaire", Component.text("Partenaire").color(NamedTextColor.GOLD).append(Component.space()), Component.empty(), NamedTextColor.GOLD),
    JOUEUR(7, 14, "Joueur", Component.text("").color(NamedTextColor.GRAY), Component.empty(), NamedTextColor.GRAY);

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

    public static Component getRankList(){
        List<Component> ranks = new ArrayList<>();

        for (RankUnit rank : values()) {
            ranks.add(Component.text(rank.getName()).color(rank.getColor()));
        }

        return Component.join(JoinConfiguration.commas(true), ranks);
    }
}
