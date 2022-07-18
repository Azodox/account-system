package net.valneas.account.rank;

/**
 * @author Azodox_ (Luke)
 * 13/7/2022.
 */
public interface RankUnit {

        String getName();
        String getPrefix();
        String getSuffix();
        String getColor();
        int getPower();
        int getId();
        boolean isDefault();
}
