package net.valneas.account.rank;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class RankUnit extends AbstractRankUnit {

    public RankUnit(String name, String prefix, String suffix, String color, int power, int id, boolean isDefault) {
        super(name, prefix, suffix, color, power, id, isDefault);
    }

    public Component name() {
        return Component.text(name);
    }

    public Component getPrefix() {
        return MiniMessage.miniMessage().deserialize(prefix);
    }

    public Component getSuffix() {
        return MiniMessage.miniMessage().deserialize(suffix);
    }

    public NamedTextColor getColor() {
        return NamedTextColor.NAMES.value(color);
    }

}
