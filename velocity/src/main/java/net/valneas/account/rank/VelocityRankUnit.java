package net.valneas.account.rank;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;

/**
 * @author Azodox_ (Luke)
 * 19/6/2022.
 */

public class VelocityRankUnit extends AbstractRankUnit {

    public VelocityRankUnit(String name, String prefix, String suffix, String color, int power, int id, boolean isDefault) {
        super(name, prefix, suffix, color, power, id, isDefault);
    }

    public Component name() {
        return Component.text(name);
    }

    public Component prefix() {
        return MiniMessage.miniMessage().deserialize(prefix);
    }

    public Component suffix() {
        return MiniMessage.miniMessage().deserialize(suffix);
    }

    public NamedTextColor color() {
        return NamedTextColor.NAMES.value(color);
    }
}
